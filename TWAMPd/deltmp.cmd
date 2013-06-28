@echo off

erase /F /S /Q ap\tmp\eaccelerator.cache\*.*
erase /F /S /Q ap\tmp\xdebug\*.*

erase /F /S /Q ap\tmp\*.*

IF EXIST ap\logs erase /F /S /Q ap\logs\*.*
IF EXIST data\*.err erase /F /S /Q data\*.err
IF EXIST data\*.pid erase /F /S /Q data\*.pid

