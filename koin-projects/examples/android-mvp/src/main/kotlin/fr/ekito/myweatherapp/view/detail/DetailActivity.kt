package fr.ekito.myweatherapp.view.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.domain.entity.DailyForecast
import fr.ekito.myweatherapp.domain.entity.getColorFromCode
import fr.ekito.myweatherapp.util.android.argument
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.scope.bindScope
import org.koin.android.scope.getActivityScope
import org.koin.core.parameter.parametersOf

/**
 * Weather Detail View
 */
class DetailActivity : AppCompatActivity(), DetailContract.View {

    // Get all needed data
    private val detailId by argument<String>(INTENT_WEATHER_ID)

    override val presenter: DetailContract.Presenter by getActivityScope().inject { parametersOf(detailId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        bindScope(getActivityScope())
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe(this)
        presenter.getDetail()
    }

    override fun onStop() {
        presenter.unSubscribe()
        super.onStop()
    }

    override fun showError(error: Throwable) {
        Snackbar.make(
                weatherItem,
                getString(R.string.loading_error) + " - $error",
                Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showDetail(weather: DailyForecast) {
        weatherIcon.text = weather.icon
        weatherDay.text = weather.day
        weatherText.text = weather.fullText
        weatherWindText.text = weather.wind.toString()
        weatherTempText.text = weather.temperature.toString()
        weatherHumidityText.text = weather.humidity.toString()
        weatherItem.background.setTint(getColorFromCode(weather))
        // Set back on background click
        weatherItem.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        const val INTENT_WEATHER_ID: String = "INTENT_WEATHER_ID"
    }
}
