1. Cuando llegue el backend REST de la biblioteca, ¿qué archivos de tu proyecto cambian y cuáles quedan intactos? 
Nómbralos y justifica con la regla de dependencia entre capas.

   Cambiaría la implementación del repositorio (crearíamos un archivo como LibroRepositorioRemoto.kt para conectarse a 
la API) y el archivo de inyección de dependencias di/AppModule.kt para registrar esta nueva clase. Quedan completamente 
intactos los modelos (Libro.kt, Lector.kt), los contratos o interfaces (LibroRepository.kt), los casos de uso 
(RegistrarLibroUseCase.kt, ListarLibrosUseCase.kt) y toda la capa visual (ViewModels y pantallas Compose). Esto se 
justifica por la regla de dependencias de Clean Architecture, la cual exige que las capas internas nunca dependan de 
las externas; como la lógica de negocio solo conoce el contrato abstracto y no la procedencia de los datos, cambiar la
memoria local por un servidor remoto no altera en absoluto el resto del sistema.

2. RegistrarLibroUseCase recibe anio y ejemplares como String. ¿Qué se perdería si los recibiera como Int? 
¿Quién tendría que hacer la conversión y por qué eso sería un problema?

   Se perdería la capacidad de centralizar las reglas de negocio y los mensajes de error dentro del dominio, impidiendo 
detectar si el usuario escribió texto no numérico, espacios en blanco o símbolos inválidos mediante 
LibroInvalidoException. La conversión (toIntOrNull()) tendría que hacerla obligatoriamente la interfaz gráfica o el 
ViewModel antes de llamar al caso de uso. Esto representaría un problema porque fugaría lógica de validación hacia la 
capa de presentación y obligaría a duplicar el mismo código de conversión en cada cliente o plataforma donde funcione la 
app (Android, iOS o web) en lugar de resolverlo en un único punto común.

3. Explica qué observaría el usuario de BiblioMobil si LibroRepository estuviera registrado en Koin como factory en vez 
de single, y por qué.

   El usuario observaría que los libros que registra desaparecen de inmediato y el catálogo sigue mostrándose vacío o sin
actualizar al volver a consultarlo. Esto ocurre porque la directiva factory le ordena a Koin crear una instancia 
totalmente nueva del repositorio cada vez que se solicita una dependencia; por lo tanto, el caso de uso de registrar
guardaría la información en una lista en memoria distinta a la que consulta el caso de uso de listar. Al configurarlo 
como single, ambas operaciones comparten exactamente la misma instancia y los datos se conservan en la sesión.