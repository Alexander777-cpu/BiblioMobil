package pe.edu.upeu.bibliomobil

import android.app.Application
import pe.edu.upeu.bibliomobil.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}