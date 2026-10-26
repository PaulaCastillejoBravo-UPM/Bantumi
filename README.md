# BANTUMI  
*Proyecto Android desarrollado en Java*  
**Autora:** Paula Castillejo Bravo

---

## 1. Descripción general
Aplicación Android del juego Bantumi desarrollada en Java. Incluye la lógica del juego, opciones de personalización, guardado y recuperación de partidas mediante ficheros y almacenamiento de puntuaciones mediante Room y SQLite.

## 2. Guardar partida
La situación de la partida se serializa mediante el método `serializa()` de `JuegoBantumi.java`. La serialización representa el contenido de las posiciones del tablero y el turno actual mediante una cadena de texto con valores separados por punto y coma (`;`). El estado serializado se guarda en un fichero de texto en el almacenamiento interno privado de la aplicación. 

Como ampliación, se permite guardar varias partidas, creando un fichero independiente para cada guardado. Los nombres de los ficheros incorporan la fecha y hora del guardado, incluidos los milisegundos, para identificar cada partida y permitir su ordenación cronológica.

## 3. Recuperar partida
Se lee el fichero de la partida guardada y se obtiene su estado serializado. Antes de ofrecer la recuperación, se compara el estado guardado con el estado actual de `JuegoBantumi`. Si ambos son idénticos, se informa al usuario y se evita una recuperación innecesaria. Si los estados son diferentes, se muestra un diálogo de confirmación (`RecoverDialog`) antes de recuperar la partida. Si el fichero no contiene datos, se informa al usuario.

Como ampliación, se permite seleccionar entre varias partidas guardadas mediante un diálogo que muestra sus fechas y horas, ordenadas de la más reciente a la más antigua. Al seleccionar una entrada, se recupera el fichero asociado. Si no existen partidas guardadas, se informa al usuario y no se muestra la lista de partidas.

## 4. Guardar puntuación
Al finalizar cada partida, se guarda un registro en la base de datos Room, basada en SQLite. Cada registro contiene el nombre del jugador configurado en los ajustes, la fecha y hora de finalización y el número de semillas restantes en cada almacén. 

La información se almacena mediante la entidad `PuntuacionPartida` y el DAO `PuntuacionPartidaDAO`, utilizando `PuntuacionPartidaRoomDatabase` para acceder a la base de datos.

La clase `PuntuacionPartidaRepositorio` se creó inicialmente, pero se eliminó posteriormente al comprobar que no se utilizaba. 

## 5. Mejores resultados
Se muestra un listado con los diez mejores resultados guardados, ordenados de mayor a menor puntuación. La pantalla incluye las columnas «Nombre» y «Puntuación».

La pantalla permite establecer filtros por jugador, fecha y número de semillas. Los filtros solo se aplican al pulsar el botón «Filtrar»; modificar sus valores sin pulsarlo no altera el listado. El botón «Quitar filtros» permite eliminar los filtros aplicados.

Se ha añadido una opción para eliminar todos los resultados almacenados, previa confirmación mediante un diálogo.

**Observaciones de la implementación:**
- Se utiliza un `RecyclerView` con el adaptador `PuntuacionPartidaListAdapter` para mostrar los resultados.
- La consulta de los diez mejores resultados se realiza mediante `getTopTen()` de `PuntuacionPartidaDAO`. Cuando se aplican filtros, se utiliza `getTopTenFiltered()`.
- Se excluyen del listado las partidas ganadas por el ordenador, para mostrar únicamente resultados de jugadores humanos.
- También se incluyen los empates entre el jugador humano y el ordenador, cuando ambos terminan con el mismo número de semillas. Esta decisión se implementa mediante el filtro `semillasAlmacen1 >= semillasAlmacen2`.

## 6. Funcionalidades opcionales
### 6.1. Número inicial de semillas
Se permite modificar el número inicial de semillas de cada casilla desde los ajustes de la aplicación, con opciones de 3 a 6 semillas. Al volver a la pantalla principal, si se ha modificado este valor, la partida se reinicia automáticamente para aplicar la nueva configuración.

### 6.2. Elección del jugador inicial
Se permite elegir desde los ajustes qué jugador realiza el primer movimiento: el jugador humano o el ordenador. Al volver a la pantalla principal, si se ha modificado esta preferencia, la partida se reinicia automáticamente para aplicar la nueva configuración.

### 6.3. Resaltado de la última casilla elegida
Se resalta la última casilla elegida por el jugador humano o por el ordenador, retirando el resaltado de la casilla anterior.

El resaltado se implementa en `MainActivity` mediante el método `resaltarCasillaSeleccionada(View casilla) `, que restaura el color de la casilla anteriormente resaltada, aplica el color de selección a la nueva casilla y actualiza la referencia a la última casilla seleccionada.

Para identificar la jugada del ordenador, se modificó el método` juegaComputador()` de `JuegoBantumi`, cambiando su tipo de retorno de void a int para que devuelva la posición elegida. `MainActivity` utiliza esa posición para localizar la vista correspondiente y resaltarla.

Durante el turno del ordenador, el usuario debe pulsar una casilla para activar su movimiento. Sin embargo, el resaltado se aplica a la casilla que el ordenador elige realmente para jugar, no a la pulsada por el usuario.

### 6.4. Personalización de colores
Se permite personalizar los colores de la aplicación desde los ajustes mediante la selección de una de las doce paletas disponibles.

Se optó por ofrecer paletas predefinidas en lugar de permitir la selección individual de colores, para que el usuario pueda comparar distintas combinaciones cromáticas y aplicarlas de forma conjunta.

La preferencia seleccionada se conserva mediante `SharedPreferences` y se aplica al volver a la pantalla principal. La actualización modifica los colores de las casillas, los almacenes, el indicador del turno activo, la casilla seleccionada y los textos. También se adaptan a la paleta elegida el botón del selector de fecha y el encabezado de la tabla de la pantalla de mejores puntuaciones.
