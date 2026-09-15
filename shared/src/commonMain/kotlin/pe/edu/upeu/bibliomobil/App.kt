package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import pe.edu.upeu.bibliomobil.presentation.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.navigation.ScreenSaver
import pe.edu.upeu.bibliomobil.presentation.prestamo.PrestamosScreen
import pe.edu.upeu.bibliomobil.presentation.theme.BiblioMobilTheme

@Composable
inline fun <reified T : Any> koinViewModel(): T = koinInject()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var modoOscuro by rememberSaveable { mutableStateOf(false) }

    KoinContext {
        BiblioMobilTheme(darkTheme = modoOscuro) {
            var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) {
                mutableStateOf<Screen>(Screen.Inicio)
            }
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "BiblioMobil",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            DESTINOS.forEach { destino ->
                                NavigationDrawerItem(
                                    label = { Text(destino.titulo) },
                                    icon = { Icon(destino.icono, contentDescription = null) },
                                    selected = pantallaActual == destino,
                                    onClick = {
                                        pantallaActual = destino
                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            HorizontalDivider()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (modoOscuro) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Modo oscuro",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                                Switch(
                                    checked = modoOscuro,
                                    onCheckedChange = { modoOscuro = it }
                                )
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(pantallaActual.titulo) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    val modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)

                    when (pantallaActual) {
                        is Screen.Inicio -> InicioScreen(
                            onNavegar = { pantallaActual = it },
                            modifier = modifier
                        )
                        is Screen.Libros -> {
                            val viewModel = koinViewModel<LibroViewModel>()
                            LibroScreen(viewModel = viewModel, modifier = modifier)
                        }
                        is Screen.Lectores -> {
                            val viewModel = koinViewModel<LectorViewModel>()
                            LectorScreen(viewModel = viewModel, modifier = modifier)
                        }
                        is Screen.Prestamos -> PrestamosScreen(modifier = modifier)
                    }
                }
            }
        }
    }
}