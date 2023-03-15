import android.content.Context
import androidx.fragment.app.FragmentManager
import com.d205.sdutyplus.view.report.dialog.CustomTimePickerDialog
import com.d205.sdutyplus.view.report.dialog.CustomTimePickerDialogClickListener

fun timePickerDialog(
    context: Context,
    startTime: String,
    parentFragmentManager: FragmentManager,
    customTimePickerDialogClickListener: CustomTimePickerDialogClickListener
) {
    val timePickerDialog = CustomTimePickerDialog(
        context,
        startTime,
        customTimePickerDialogClickListener
    )
    timePickerDialog.show(parentFragmentManager, "TimePicker")
}