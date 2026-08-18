@echo off
title Nuevo semestre, nueva carpeta

dir C:\Users\vmvic\Desktop\CUCEI
echo.
echo Verifica el espacio en carpeta. 
echo.

echo Estas seguro de que deseas crear las nuevas carpetas?
choice /c SN /m "Presiona S para continuar o N para cancelar"

if errorlevel 2 goto :cancelar
if errorlevel 1 goto :crear

:crear
echo.
echo Creando directorios...
cd /d "C:\Users\vmvic\Desktop\CUCEI"
mkdir "TAREAS n SEMESTRE\Recursos"
echo Carpetas creadas exitosamente.
pause
exit

:cancelar
echo.
echo Operacion cancelada por el usuario.
pause
exit