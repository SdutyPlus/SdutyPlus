package com.d108.sduty.ui.camstudy.preview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d108.sduty.common.*
import com.d108.sduty.databinding.FragmentPreviewBinding
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.camstudy.room.RoomActivity
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.Status
import com.google.common.util.concurrent.ListenableFuture
import com.sendbird.calls.SendBirdCall

private const val TAG ="PreviewFragment"
class PreviewFragment : Fragment() {
    private lateinit var binding : FragmentPreviewBinding
    private val viewModel : PreviewViewModel by viewModels()
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private var preview: Preview? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: PreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = binding.previewPreviewView

        setViewEventListeners()
        observeViewModel()
        initCameraPreview()        
    }


    private fun initCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setViewEventListeners() {
        binding.previewEnterButton.setOnClickListener(this::onEnterButtonClicked)
        binding.previewImageViewClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.previewAudioCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){

            }
        }
        binding.previewVideoCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.previewPreviewView.visibility = View.INVISIBLE
                binding.imgPreview.visibility = View.VISIBLE
            } else{
                binding.previewPreviewView.visibility = View.VISIBLE
                binding.imgPreview.visibility = View.INVISIBLE
            }

        }

    }


    private fun goToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun observeViewModel() {
        viewModel.authenticationLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    Log.d(TAG, "observeViewModel: LOADING")
                }
                Status.SUCCESS -> {
                    val roomId = args.roomId
                    Log.d(TAG, "observeViewModelrroomm: ${args.roomId}")

//                  viewModel.enter(roomId, isAudioEnabled, isVideoEnabled)
                    viewModel.fetchRoomById(roomId)
                }
                Status.ERROR -> Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchedRoomId.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    val isAudioEnabled = !binding.previewAudioCheckbox.isChecked
                    val isVideoEnabled = !binding.previewVideoCheckbox.isChecked
                    viewModel.enter(it.data.toString(), isAudioEnabled, isVideoEnabled)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "ERROR / authViewModel.fetchedRoomId", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        viewModel.enterResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> goToRoomActivity()
                Status.ERROR -> {
                    requireActivity().setResult(RESULT_ENTER_FAIL, Intent().apply {
                        putExtra(EXTRA_ENTER_ERROR_CODE, resource.errorCode)
                        putExtra(EXTRA_ENTER_ERROR_MESSAGE, resource.message)
                    })
                }
                else -> {}
            }
        }
    }

    private fun onEnterButtonClicked(v: View) {
        SendBirdCall.init(requireActivity().applicationContext, SENDBIRD_APP_ID)
        viewModel.authenticate(mainViewModel.profile.value!!.nickname)
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        preview = Preview.Builder().build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview?.setSurfaceProvider(previewView.surfaceProvider)

        var camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)



    }

    private fun goToRoomActivity() {
        Log.d(TAG, "goToRoomActivity: ${args.roomId}")
        val roomId = args.roomId
        val intent = Intent(requireContext(), RoomActivity::class.java).apply {
            putExtra(EXTRA_ROOM_ID, roomId)
            putExtra(EXTRA_IS_NEWLY_CREATED, false)
            putExtra("studyName", args.studyName)
        }
        startActivity(intent)
    }

}