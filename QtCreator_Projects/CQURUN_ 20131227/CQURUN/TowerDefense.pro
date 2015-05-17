#-------------------------------------------------
#
# Project created by QtCreator 2013-12-22T22:46:33
#
#-------------------------------------------------

QT       += core gui multimedia

CONFIG += c++11

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = TowerDefense
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    towerposition.cpp \
    tower.cpp \
    waypoint.cpp \
    enemy.cpp \
    bullet.cpp \
    audioplayer.cpp \
    plistreader.cpp \
    Add_aboutus/about_us.cpp

HEADERS  += mainwindow.h \
    towerposition.h \
    tower.h \
    waypoint.h \
    utility.h \
    enemy.h \
    bullet.h \
    audioplayer.h \
    plistreader.h \
    Add_aboutus/about_us.h

FORMS    += mainwindow.ui \
    Add_aboutus/about_us.ui

RESOURCES += \
    resource.qrc \
    Add_aboutus/myResource.qrc \
    Add_aboutus/myResource.qrc
