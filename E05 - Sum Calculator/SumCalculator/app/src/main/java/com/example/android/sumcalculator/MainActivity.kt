package com.example.android.sumcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private var inputString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun numberInput(view: View) {
        inputString += (view as Button).text.toString()
        resultTextView.text = inputString

    }

    fun calcAndPrintResult(view: View) {
        val expression = ExpressionBuilder(inputString).build()
        try {
            val result = expression.evaluate().toInt()
            resultTextView.text = "${inputString} =  ${result}"
        } catch (ex: ArithmeticException) {
            resultTextView.text = "Error"
        }
    }

    fun clearInput(view: View) {
        inputString = ""
        resultTextView.text = inputString
    }
}
