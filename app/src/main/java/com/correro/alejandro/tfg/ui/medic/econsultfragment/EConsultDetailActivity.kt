package com.correro.alejandro.tfg.ui.medic.econsultfragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR
import com.correro.alejandro.tfg.BuildConfig
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Respuesta
import com.correro.alejandro.tfg.data.api.models.econsultdetailresponse.EConsultDetail
import com.correro.alejandro.tfg.data.api.models.econsultdetailresponse.EconsultDetailResponse
import com.correro.alejandro.tfg.data.api.models.econsultdetailresponse.EconsultInfo
import com.correro.alejandro.tfg.databinding.ActivityEconsultDetailBinding
import com.correro.alejandro.tfg.ui.medic.searchfragment.SearchDetailActivity
import com.correro.alejandro.tfg.ui.patient.consultfragment.ConsultDetailPhotoAdapter
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import com.correro.alejandro.tfg.utils.permissionWrite


import kotlinx.android.synthetic.main.activity_econsult_detail.*
import java.util.ArrayList

class EConsultDetailActivity : AppCompatActivity() {
    private lateinit var mviewmodel: EConsultDetailActivityViewModel

    private lateinit var mBinding: ActivityEconsultDetailBinding
    private lateinit var econsultI: EConsultDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_econsult_detail);
        setupToolbar()

        mviewmodel = ViewModelProviders.of(this).get(EConsultDetailActivityViewModel::class.java)
        mviewmodel.idEConsult = intent.getIntExtra(EXTRA_ID_ECONSULT, -1)
        mviewmodel.getEconsult()
        mviewmodel.econsult.observe(this, Observer { initViews(it!!) })
        mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Error") })


    }

    private lateinit var adapter: ConsultDetailPhotoAdapter

    private lateinit var adapterResponses: GenericAdapter<Respuesta>


    private fun initViews(econsultInfo: EconsultInfo) {
        econsultI = econsultInfo.EConsult
        var params = nstPhotos.layoutParams
        if (econsultInfo.attachments.size == 0)
            params.height = 0
        nstPhotos.layoutParams = params

        adapter = ConsultDetailPhotoAdapter(setlist())
        rcyPhotos.layoutManager = GridLayoutManager(this, 2)
        rcyPhotos.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rcyPhotos.adapter = adapter
        for (a in econsultInfo.attachments)
            mviewmodel.downloadFile(a.adjunto, this).observe(this, Observer { it -> adapter.addItem(it) })
        adapterResponses = GenericAdapter(BR.response, R.layout.fragment_consult_medic_type_item, null, null, econsultInfo.respuestas as ArrayList<Respuesta?>)

        var layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        rcyResponses.layoutManager = layoutManager
        rcyResponses.adapter = adapterResponses

        btnSend.setOnClickListener { clickValues() }

        mBinding.econsultdetail = econsultInfo.EConsult

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "Detalle Econsulta"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
    }

    private var item: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflator = menuInflater
        inflator.inflate(R.menu.consult_detail, menu)
        item = menu!!.findItem(R.id.mnuDetail)
        item!!.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mnuDetail -> {
                if (::econsultI.isInitialized)
                    SearchDetailActivity.start(this, econsultI.idPaciente.toInt())
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun clickValues() {
        if (!TextUtils.isEmpty(txtResponse.text)) {
            val response: String = txtResponse.text.toString()
            mviewmodel.newResponse(response).observe(this, Observer { setresult(it) })

        }
    }

    private fun setresult(it: Respuesta?) {
        txtResponse.text=null
        adapterResponses.items.add(0, it!!)
        adapterResponses.notifyItemInserted(0)
        rcyResponses.scrollToPosition(0)

    }

    private fun setlist(): (FileWithType) -> Unit {

        return {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it.file);
            intent.setDataAndType(uri, it.type)
            this.permissionWrite { this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1) }
        }
    }

    companion object {
        private const val EXTRA_ID_ECONSULT = "EXTRA_ID_CONSULT"
        fun start(context: Context, id_consulta: Int) {
            val intent = Intent(context, EConsultDetailActivity::class.java)
            intent.putExtra(EXTRA_ID_ECONSULT, id_consulta)
            context.startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
