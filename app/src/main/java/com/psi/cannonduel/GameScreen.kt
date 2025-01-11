package com.psi.cannonduel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Clase para encapsular el estado de los jugadores
data class PlayerState(
    var hp: Int,
    var ammo: Int,
    var fuel: Int,
    var position: Pair<Int, Int>
)

// Constantes con valores por defecto
const val MAX_HP = 10
const val MAX_AMMO = 30
const val MAX_FUEL = 100
const val GRID_SIZE = 10

// Función para la pantalla de juego
@Composable
fun GameScreen() {
    // Estados de los jugadores
    val player1State =
        remember { mutableStateOf(PlayerState(MAX_HP, MAX_AMMO, MAX_FUEL, Pair(9, 9))) }
    val player2State =
        remember { mutableStateOf(PlayerState(MAX_HP, MAX_AMMO, MAX_FUEL, Pair(0, 0))) }
    // Dirección del viento
    val windDirection = remember { mutableStateOf("N") }
    // Fuerza del viento
    val windStrength = remember { mutableIntStateOf(0) }
    // Casilla seleccionada
    val selectedCell = remember { mutableStateOf<Pair<Int, Int>?>(null) }
    // Munición seleccionada
    val selectedAmmo = remember { mutableStateOf("Ammo 1") }
    // Estado del grid
    val gridState =
        remember { mutableStateOf(Array(GRID_SIZE) { Array(GRID_SIZE) { true } }) } // True -> casilla disponible, false -> casilla destruída
    // Texto del botón de acción
    val actionButtonText = remember { mutableStateOf("Shoot") }
    // Texto de la caja de información
    val infoMessage = remember { mutableStateOf("Choose a target") }

    // Contenedor que ocupa toda la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        // Barra inferior para el jugador 1
        PlayerBar(
            playerName = "Player 1",
            progress = player1State.value.hp / MAX_HP.toFloat(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Barra superior para el jugador 2
        PlayerBar(
            playerName = "Player 2",
            progress = player2State.value.hp / MAX_HP.toFloat(),
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Contenedor central para grid, caja de información y barra de combustible
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título del viento
            WindInfo(direction = windDirection.value, strength = windStrength.intValue)

            // Grid 10x10
            GameGrid(
                player1Position = player1State.value.position,
                player2Position = player2State.value.position,
                selectedCell = selectedCell.value,
                gridState = gridState.value,
                onCellClick = { selectedCell.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Caja de información
            InfoBox(infoText = infoMessage.value)

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de combustible
            FuelBar(fuelLevel = player1State.value.fuel / MAX_FUEL.toFloat())
        }

        // Contenedor inferior para selector de munición y botón de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selector de munición
            AmmoSelector(
                selectedAmmo = selectedAmmo.value,
                onAmmoChange = { selectedAmmo.value = it }
            )

            // Botón de acción
            ActionButton(
                actionText = actionButtonText.value,
                onActionClick = {
                    handleActionButtonClick(
                        actionText = actionButtonText.value,
                        selectedCell = selectedCell.value,
                        player1State = player1State.value,
                        player2State = player2State.value,
                        gridState = gridState.value,
                        windDirection = windDirection.value,
                        windStrength = windStrength.intValue,
                        onInfoUpdate = { message -> updateInfoMessage(infoMessage, message) },
                        onActionChange = { actionButtonText.value = it },
                        onClearSelection = { selectedCell.value = null }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen() {
    GameScreen()
}