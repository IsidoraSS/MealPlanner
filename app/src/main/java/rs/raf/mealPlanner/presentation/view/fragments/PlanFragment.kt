package rs.raf.mealPlanner.presentation.view.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.entities.PlanEntity
import rs.raf.mealPlanner.data.models.utils.DailyPlan
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.data.models.utils.TableData
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.states.SavePlanState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel
import timber.log.Timber


class PlanFragment : Fragment(), DialogInterface.OnDismissListener {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private lateinit var planEntity: PlanEntity
    private var daysList = mutableListOf<DailyPlan>()
    private var dialogSelectedMeal:Meal?=null
    private var tableData:TableData= TableData(0,0)
    private lateinit var linearLayout: LinearLayout
    private lateinit var tableLayout: TableLayout
    private lateinit var button: Button
    private lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        linearLayout = LinearLayout(requireContext())
        initLinearLayout(linearLayout)
        return linearLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initData()
        initObservers()
    }

    private fun initData(){
        for (i in 0 until 7) {
            daysList.add(DailyPlan(null,null,null,null))
        }
    }

    private fun initObservers(){
        mainViewModel.planDialogSelectedMeal.observe(viewLifecycleOwner, Observer{
            dialogSelectedMeal=it
        })

//        mainViewModel.planSaved.observe(viewLifecycleOwner,Observer{
//            renderState(it)
//        })
    }

    private fun initLinearLayout(linearLayout: LinearLayout) {
        // Create a RelativeLayout as the parent layout
        val parentLayout = RelativeLayout(requireContext())
        parentLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        // Create the TableLayout
        tableLayout = TableLayout(requireContext())
        val tableLayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT // Adjust height as needed
        )
        tableLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP) // Align to the top
        tableLayout.layoutParams = tableLayoutParams

        initTableLayout(tableLayout) // Initialize your TableLayout as before

        // Create a Button
        button = Button(requireContext())
        val buttonLayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) // Align to the bottom
        buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL) // Center horizontally
        button.layoutParams = buttonLayoutParams
        button.text = resources.getString(R.string.save)

        button.setOnClickListener{
            savePlan()
        }

        // Add the TableLayout and Button to the parent layout
        parentLayout.addView(tableLayout)
        parentLayout.addView(button)

        linearLayout.addView(parentLayout)
    }

    private fun initTableLayout(tableLayout: TableLayout) {
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableLayout.gravity = Gravity.CENTER
        tableLayout.id="99".toInt()

        // Add an EditText row at the top
        val editTextRow = TableRow(context)
        editTextRow.setBackgroundColor(Color.WHITE)
        val editTextRowParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        editTextRowParams.setMargins(1, 1, 1, 1)
        editTextRow.layoutParams = editTextRowParams

        editText = EditText(context)
        editText.hint = "Plan name"
        editText.setBackgroundColor(Color.WHITE)
        val editTextParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        editTextParams.weight = 1f // This will make the EditText expand to fill the row
        editText.layoutParams = editTextParams

        // Add the EditText to the EditText row
        editTextRow.addView(editText)

        // Add the EditText row to the table
        tableLayout.addView(editTextRow)


        //first row initialization
        val row1 = TableRow(context)
        row1.setBackgroundColor(Color.BLACK)
        val rowLayoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
        rowLayoutParams.setMargins(1, 1, 1, 1)
        row1.layoutParams = rowLayoutParams

        //first row columns
        val textView0 = createTextView("")
        textView0.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_secondary))

        val textView1 = createTextView("Breakf")
        textView1.setBackgroundColor(Color.WHITE)

        val textView2 = createTextView("Lunch")
        textView2.setBackgroundColor(Color.WHITE)

        val textView3 = createTextView("Dinner")
        textView3.setBackgroundColor(Color.WHITE)

        val textView4 = createTextView("Snack")
        textView4.setBackgroundColor(Color.WHITE)

        //populate first row
        row1.addView(textView0)
        row1.addView(textView1)
        row1.addView(textView2)
        row1.addView(textView3)
        row1.addView(textView4)

        tableLayout.addView(row1)

        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        for (day in daysOfWeek) {
            val tableRow = TableRow(requireContext())
            tableRow.id=daysOfWeek.indexOf(day)

            val dayName = createTextView(day)

            //init textviews
            val breakfastTextView = createTextView("")
            breakfastTextView.tag=(TableData(daysOfWeek.indexOf(day),0))
            breakfastTextView.setOnClickListener {
                onTextViewClick(breakfastTextView)
            }
            val lunchTextView = createTextView("")
            lunchTextView.tag=(TableData(daysOfWeek.indexOf(day),1))
            lunchTextView.setOnClickListener{
                onTextViewClick(lunchTextView)
            }
            val dinnerTextView = createTextView("")
            dinnerTextView.tag=(TableData(daysOfWeek.indexOf(day),2))
            dinnerTextView.setOnClickListener{
                onTextViewClick(dinnerTextView)
            }
            val snackTextView = createTextView("")
            snackTextView.tag=(TableData(daysOfWeek.indexOf(day),3))
            snackTextView.setOnClickListener{
                onTextViewClick(snackTextView)
            }

            //row height
            val rowHeightInDp = 48
            val density = resources.displayMetrics.density
            val rowHeight = (rowHeightInDp * density).toInt()
            tableRow.minimumHeight = rowHeight

            //add textviews
            tableRow.addView(dayName)
            tableRow.addView(breakfastTextView)
            tableRow.addView(lunchTextView)
            tableRow.addView(dinnerTextView)
            tableRow.addView(snackTextView)

            tableLayout.addView(tableRow)
        }
    }

    private fun savePlan(){
        if(!editText.text.toString().isNullOrEmpty()){
            val planToSave=PlanEntity(editText.text.toString(), daysList)
            mainViewModel.savePlan(planToSave)
        }
        else{
            Toast.makeText(context,"Enter plan name first", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun renderState(state: SavePlanState) {
        when(state) {
            is SavePlanState.Success -> Toast.makeText(context, "Plan saved", Toast.LENGTH_SHORT)
                .show()
            is SavePlanState.Error -> Toast.makeText(context, "Error happened", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onTextViewClick(textView: TextView) {
        val td = textView.getTag() as TableData
        tableData=td
        val dialog=DialogPlanFilter()
        dialog.show(childFragmentManager, "Select meal for the day")
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(requireContext())
        textView.text = text
        textView.textSize = 18f
        textView.gravity = Gravity.CENTER
        val layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.MATCH_PARENT,
            1f
        )
        layoutParams.setMargins(1, 1, 1, 1)
        textView.layoutParams = layoutParams
        textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.cell_border)
        return textView
    }

    private fun showDialog(){
        val dialog=DialogPlanFilter()
        dialog.show(childFragmentManager, "Select meal for the day")
    }

    fun test(){
        Timber.e(dialogSelectedMeal!!.name)
    }

    override fun onDismiss(p0: DialogInterface?) {
        when(tableData.rowIndex){
            0->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(0).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(0).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(0).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(0).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            1->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(1).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(1).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(1).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(1).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            2->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(2).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(2).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(2).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(2).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            3->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(3).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(3).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(3).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(3).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            4->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(4).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(4).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(4).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(4).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            5->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(5).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(5).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(5).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(5).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
            6->{
                when(tableData.columnIndex){
                    0->{
                        daysList.get(6).breakfastMealId= dialogSelectedMeal?.id
                    }
                    1->{
                        daysList.get(6).lunchMealId= dialogSelectedMeal?.id
                    }
                    2->{
                        daysList.get(6).dinnerMealId= dialogSelectedMeal?.id
                    }
                    3->{
                        daysList.get(6).snackMealId= dialogSelectedMeal?.id
                    }
                }
            }
        }
        var row = tableLayout.findViewById<View>(tableData.rowIndex)
        var textView: TextView = row.findViewWithTag(tableData)
        textView.text= dialogSelectedMeal?.name
        test()
    }
}
