#ifndef ABOUT_US_H
#define ABOUT_US_H


#include <QDialog>
#include "qmovie.h"
#include "qlabel.h"

namespace Ui {
class About_us;
}

class About_us : public QDialog
{
    Q_OBJECT

public:
    explicit About_us(QWidget *parent = 0);
    ~About_us();

private:
    Ui::About_us *ui;
};

#endif // ABOUT_US_H
