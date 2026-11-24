package com.example.releasekit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity() {
    private lateinit var store: TaskStore
    private lateinit var tasks: TaskList
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var updateManager: AppUpdateManager
    private lateinit var btnSort: Button
    private lateinit var btnShare: Button

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            Toast.makeText(this, R.string.update_cancelled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyWindowInsets()

        store = TaskStore(this)
        tasks = store.load()
        updateManager = AppUpdateManagerFactory.create(this)

        findViewById<TextView>(R.id.tvBuildInfo).text = getString(
            R.string.build_info,
            BuildConfig.APPLICATION_ID,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
            BuildConfig.BUILD_TYPE,
            BuildConfig.API_BASE_URL,
        )

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks.items.toMutableList())
        findViewById<ListView>(R.id.listTasks).apply {
            this.adapter = this@MainActivity.adapter
            setOnItemLongClickListener { _, _, position, _ ->
                tasks.removeAt(position)
                persistAndRefresh()
                true
            }
        }

        val input = findViewById<EditText>(R.id.etTask)
        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            if (tasks.add(input.text.toString())) {
                input.text.clear()
                persistAndRefresh()
                Analytics.log(this, "task_added")
            }
        }

        btnSort = findViewById(R.id.btnSort)
        btnSort.setOnClickListener {
            tasks.sortAlphabetically()
            persistAndRefresh()
            Analytics.log(this, "tasks_sorted")
        }

        btnShare = findViewById(R.id.btnShare)
        btnShare.setOnClickListener {
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, tasks.toShareText())
            }
            startActivity(Intent.createChooser(send, getString(R.string.share)))
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener { checkForUpdate(manual = true) }

        findViewById<Button>(R.id.btnCrash).apply {
            visibility = if (BuildConfig.ENABLE_TEST_CRASH) View.VISIBLE else View.GONE
            setOnClickListener { CrashReporter.crashForTest() }
        }

        applyFlags()
        FeatureFlags.init {
            applyFlags()
            checkForUpdate(manual = false)
        }
    }

    private fun applyFlags() {
        btnSort.visibility = if (FeatureFlags.isEnabled(FeatureFlags.SORT_TASKS)) View.VISIBLE else View.GONE
        btnShare.visibility = if (FeatureFlags.isEnabled(FeatureFlags.SHARE_TASKS)) View.VISIBLE else View.GONE
    }

    private fun persistAndRefresh() {
        store.save(tasks)
        adapter.clear()
        adapter.addAll(tasks.items)
    }

    /** In-app updates + принудительное обновление по min_supported_version_code. */
    private fun checkForUpdate(manual: Boolean) {
        updateManager.appUpdateInfo
            .addOnSuccessListener { info ->
                val available = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                val action = UpdatePolicy.decide(
                    currentVersionCode = BuildConfig.VERSION_CODE,
                    minSupportedVersionCode = FeatureFlags.minSupportedVersionCode().toInt(),
                    availableVersionCode = if (available) info.availableVersionCode() else null,
                    priority = info.updatePriority(),
                )
                val type = when (action) {
                    UpdateAction.IMMEDIATE -> AppUpdateType.IMMEDIATE
                    UpdateAction.FLEXIBLE -> AppUpdateType.FLEXIBLE
                    UpdateAction.NONE -> {
                        if (manual) toast(R.string.no_update)
                        return@addOnSuccessListener
                    }
                }
                if (available && info.isUpdateTypeAllowed(type)) {
                    updateManager.startUpdateFlowForResult(
                        info,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(type).build(),
                    )
                }
            }
            .addOnFailureListener { if (manual) toast(R.string.update_check_failed) }
    }

    private fun toast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

    /** targetSdk 35+ включает edge-to-edge: добавляем отступы под системные панели. */
    private fun applyWindowInsets() {
        val root = findViewById<View>(R.id.root)
        val pad = (BASE_PADDING_DP * resources.displayMetrics.density).toInt()
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(pad + bars.left, pad + bars.top, pad + bars.right, pad + bars.bottom)
            insets
        }
    }

    private companion object {
        const val BASE_PADDING_DP = 16
    }
}
