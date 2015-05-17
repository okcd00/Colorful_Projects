#include "about_us.h"
#include "ui_about_us.h"
#include <QLabel>
#include <QMovie>
//Edited by CD 20125209

About_us::About_us(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::About_us)
{
    ui->setupUi(this);
    QMovie *moviebgd = new QMovie("://Add_aboutus/bgd.gif");
    ui->Giflabel->setMovie(moviebgd);
    QMovie *moviecd = new QMovie("://Add_aboutus/dian.gif");
    ui->CDlabel->setMovie(moviecd);
    QMovie *moviekl = new QMovie("://Add_aboutus/gali2.gif");
    ui->KLlabel->setMovie(moviekl);
    QMovie *movieyr = new QMovie("://Add_aboutus/baizi.jpg");
    ui->YRlabel->setMovie(movieyr);
    moviebgd->start();
    moviecd->start();
    moviekl->start();
    movieyr->start();
}

About_us::~About_us()
{
    delete ui;
}
/*
About_us::About_us(QWidget *parent) :
    QDialog(parent),

    ui(new Ui::About_us)
{
    ui->setupUi(this);

}

About_us::~About_us()
{
    delete ui;
}
*/
