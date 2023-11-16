package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.DateCount

sealed class DateCountState{
    data class Success(val weeklyCount: List<DateCount>): DateCountState()
    data class Error(val message: String): DateCountState()
}