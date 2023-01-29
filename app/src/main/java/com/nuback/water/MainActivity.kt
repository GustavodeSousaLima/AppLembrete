package com.nuback.water

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.lembrete.lembretetomargua.R
import com.lembrete.lembretetomargua.databinding.ActivityMainBinding
import com.nuback.water.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var CalcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoMl = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        //IniciarComponentes()
        CalcularIngestaoDiaria = CalcularIngestaoDiaria()

        binding.btCalcular.setOnClickListener {

            if (binding.editPeso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()
            } else if (binding.editIdade.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()
            } else {
                val peso = binding.editPeso.text.toString().toDouble()
                val idade = binding.editIdade.text.toString().toInt()
                CalcularIngestaoDiaria.CalcularTotalMl(peso, idade)
                resultadoMl = CalcularIngestaoDiaria.ResultadoMl()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                binding.txtResultadoMl.text = formatar.format(resultadoMl) + " " + "ml"
            }
        }

        binding.icRestart.setOnClickListener {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialog_titulo)
                .setMessage(R.string.dialog_desc)
                .setPositiveButton("ok", {dialogInterface, i ->
                    binding.editPeso.setText("")
                    binding.editIdade.setText("")
                    binding.txtResultadoMl.text = ""
                })

            alertDialog.setNegativeButton("Cancelar", { dialogInterface, i ->

            })

            val dialog = alertDialog.create()
            dialog.show()

        }

        binding.btDefinirLembrete.setOnClickListener {

            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog =
                TimePickerDialog(this, { timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                    binding.txtHora.text = String.format("%02d", hourOfDay)
                    binding.txtMinutos.text = String.format("%02d", minutes)
                }, horaAtual, minutosAtuais, true)
            timePickerDialog.show()

        }

        binding.btAlarme.setOnClickListener {

            val txt_hora = binding.txtHora
            val txt_minutos = binding.txtMinutos

            if (!txt_hora.toString().isEmpty() && !txt_minutos.toString().isEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES,txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_mensagem))
                startActivity(intent)

                if (intent.resolveActivity(packageManager)!= null){
                    startActivity(intent)
                }
            }
        }
    }

    /*private fun IniciarComponentes() {
         edit_peso = findViewById(R.id.edit_peso)
         edit_idade = findViewById(R.id.edit_idade)
         bt_calcular = findViewById(R.id.bt_calcular)
         txt_resultado_ml = findViewById(R.id.txt_resultado_ml)
         ic_redefinir_dados = findViewById(R.id.ic_restart)
         bt_lembrete = findViewById(R.id.bt_definir_lembrete)
         bt_alarme = findViewById(R.id.bt_alarme)
         bt_calcular = findViewById(R.id.bt_calcular)
         txt_hora = findViewById(R.id.txt_hora)
         txt_minutos = findViewById(R.id.txt_minutos)
    */
}