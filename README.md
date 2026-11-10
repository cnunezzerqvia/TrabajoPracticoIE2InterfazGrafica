# Simulador de Ecosistema — versión Swing (2.ª instancia evaluativa)

Transformación del simulador de la IE1 (que corría en la terminal) en una aplicación de escritorio
con interfaz gráfica en **Swing**, arquitectura **MVC en paquetes separados** y guardado de la
simulación con un **DAO sobre archivo de texto plano (.txt)**.

## Cómo ejecutarlo

- **Apache NetBeans** 22 o superior (proyecto Maven).
- **JDK 21** (el `pom.xml` usa `maven.compiler.release` 21).
- No usa librerías externas: solo Java estándar + Swing.
- Abrir la carpeta `TrabajoPractico` en NetBeans → *Run Project* (F6).
  La clase principal es `com.mycompany.trabajopractico.TrabajoPractico`.
- La partida se guarda sola en `datos/simulacion.txt` (ruta relativa, dentro de la carpeta del
  proyecto). Al volver a abrir la aplicación se retoma donde quedó.

## Estructura del proyecto

| Paquete | Qué contiene |
|---|---|
| `com.mycompany.trabajopractico` (principal) | `TrabajoPractico`: el `main`. Crea la vista, el DAO y el controlador, los conecta y muestra la ventana. |
| `...modelo` | Clases de la IE1: `Entidad` (abstracta), `Animal` (abstracta), `Planta`, `Conejo`, `Lobo`, interfaces `Mortal` y `Reproducible`, enum `Clima`, `Ecosistema`. Más `Simulacion` (reglas de fin e intervención) y `Estadisticas` (datos del reporte). Sin `Scanner`, sin `System.out` y sin Swing: los eventos del turno quedan en una lista. |
| `...dao` | `SimulacionDAO` (interfaz) y `SimulacionDAOTxt`: guarda y lee la simulación en `.txt`, maneja los errores de entrada/salida y devuelve el resultado sin mostrar mensajes. |
| `...controlador` | `ControladorSimulacion`: recibe las acciones de la vista, valida los datos (vacíos, formato, rangos), coordina modelo y DAO y le entrega a la vista los datos actualizados. Sin código Swing. |
| `...vista` | `VentanaPrincipal` (JFrame hecho con el diseñador de NetBeans, con 3 pantallas en un `CardLayout`) e `IVistaEcosistema`, la interfaz con la que el controlador le habla a la vista. |

## Pantallas

1. **Configuración**: formulario con plantas (5–30), conejos (2–15), lobos (1–5), clima y turnos (10–50).
   Los errores se muestran en rojo debajo del formulario.
2. **Simulación**: contadores arriba (turno, clima, plantas, conejos, lobos), tabla de entidades vivas
   en el centro, lista de eventos del turno a la derecha, y abajo el panel de intervención, el botón
   *Avanzar turno* y la barra de estado.
3. **Reporte final**: causa de fin, resumen general, datos por tipo (nacimientos, muertes, más longeva,
   población máxima y mínima) e historial de población por turno.

Menú superior: **Simulación** (Nueva simulación, Guardar ahora, Salir) y **Reportes** (Ver reporte final).

## Integrantes

| Integrante | Usuario de GitHub |
|---|---|---|
| (Cesar Nuñez) | (cnunezzerqvia) |
| (Pedro Dominguez) | (pdominguezzervia) |
| (Carlos Ponce) | (carlosponce45) |
| (Alexis Rosales) | (alexis rosales) |

## Links

Figma:https://www.figma.com/design/LTsPjy8VNxyFOdA4jgDOFz/Sin-t%C3%ADtulo?node-id=3-51&t=g3huDdoNuvbHd91Y-1

## Desafíos encontrados

- El modelo de la IE1 imprimía todo con `System.out.println`. Lo reemplazamos por una lista de eventos
  dentro de `Ecosistema` que el controlador le pide y le pasa a la vista.
- En la IE1 los nombres se armaban con el tamaño de la lista y se repetían cuando moría alguien
  (dos "Conejo-3"). Ahora el ecosistema lleva contadores que también se guardan en el archivo.
- La vista no puede crear componentes por código: los combos se llenan con datos que manda el
  controlador (`cargarClimas`), y las tablas se cargan con `addRow` sobre el modelo creado en el diseñador.
- El archivo puede no existir o estar dañado: el DAO devuelve `null` con la descripción del error y el
  controlador decide mostrar un mensaje y ofrecer una simulación nueva.

## Uso de IA y documentación

Se utilizó Claude para ayuda en algunas partes del código (están los prompts en la carpeta documentación) y para comentar el readme y el código completo para ayuda a la hora de explicar el video sin trabas.

## Formato del archivo `datos/simulacion.txt`

Una línea por dato, con el tipo primero y los valores separados por `;`:

```
CONFIG;20;6;SOLEADO;false      turnos totales; turno actual; clima; intervención usada
IDS;14;9;3                     últimos números usados en los nombres
TIPO;Planta;4;6;Planta-2;6     tipo; nacimientos; muertes; más longeva; edad
RECORDS;Lobo-1;3;4;12          lobo con más cacerías; cacerías; turno más activo; eventos
HISTORIAL;6;12;5;2             turno; plantas; conejos; lobos
PLANTA;Planta-7;45.0;2;3       nombre; energía; edad; tamaño
CONEJO;Conejo-3;60.0;4         nombre; energía; edad
LOBO;Lobo-1;70.0;6;3           nombre; energía; edad; cacerías
EVENTO;Conejo-3 comió a ...    eventos del último turno
```
