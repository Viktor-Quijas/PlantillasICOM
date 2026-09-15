Machine Learning



09-09-2026



#### Evaluación



**- Matriz de confusión**



La dimensión de la matriz es igual:



&#x09;n = la cantidad de clases

&#x09;dimensiones = n \* n



Y matrices como clases tengamos.



Real vs Predicción



Fila = falsos negativos

Columna = Falsos positivos



**- Métricas de desempeño**



Precisión = TP / (TP + FP)	: Precisión del modelo sobre los positivos.



Sensibilidad  = TP / (TP + FN)	: Lo bueno que fue acertando en la clase A sobre la B



Especificidad = TN / (TN + FP)	: Complemento a sensibilidad



NEGATIVE PREDICTIC VALUE (NPV) = TN / (TN + FN)	: Precisión para negativos.



Accuracy = (TP + TN) / (TP + FN + FP + TN)	: Precisión total.



**Nuevas métricas**



F1-score: media armonica entre precisión sobre sensibilidad

&#x09;Media armonica = 2 \* Precisión \* Sensibilidad / (Precisión + Sensibilidad)



#### Validación



**-Técnicas de validación**



Validación cruzada (hold out):



&#x09;1. Revolver (Revolving chaos chaos) la base de datos.

&#x09;2. Dividir base de datos en dos conjuntos:

&#x09;	a. Entrenar  	70%

&#x09;	b. Probar	30%

&#x09;3. Ejecutar varias veces

&#x09;	a. De cinco a diez.



Leave-one-out:



&#x09;1. Revolver (Revolving chaos chaos) la base de datos.

&#x09;2. Dividir entrenamiento y prueba

&#x09;	a. Entrenar	99%

&#x09;	b. Probar	01%

&#x09;3. Ejecutar n veces



K-fold cros validation

&#x09;

&#x09;1 Revolver base de datos

&#x09;2. Decidir cantidad de carpetas, recomendado k > 5 y divisible entre la cantidad de datos.

&#x09;3. Las carpetas son las divisiones de mi base de datos.

&#x09;4. Dividir entrenamiento y prueba

&#x09;	a. Entrenar	k - 1

&#x09;	b. Probar	Una carpeta

&#x09;5. Ejecutar k veces



Time Series Split: Cuando importa el tiempo



&#x09;1. Se divide en carpetas, su tamaño está definido por intervalos de tiempo y no se mezclan los datos entre si.

&#x09;2. Dividir entrenamiento y prueba

&#x09;	a. Entrenar	k - 1

&#x09;	b. Probar	Una carpeta

&#x09;3. Ejecutar k veces



Estratificado: 

&#x09;Tener la misma representación de A y B en entrenar y probar.



