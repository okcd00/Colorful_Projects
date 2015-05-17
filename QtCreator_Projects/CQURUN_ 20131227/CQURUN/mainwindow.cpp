#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "waypoint.h"
#include "enemy.h"
#include "bullet.h"
#include "audioplayer.h"
#include "plistreader.h"
#include "Add_aboutus/about_us.h"
#include <QPainter>
#include <QMouseEvent>
#include <QtGlobal>
#include <QMessageBox>
#include <QTimer>
#include <QXmlStreamReader>
#include <QtDebug>

static const int TowerCost = 300;
bool gamestart=false;

MainWindow::MainWindow(QWidget *parent)
	: QMainWindow(parent)
	, ui(new Ui::MainWindow)
	, m_waves(0)
	, m_playerHp(5)
	, m_playrGold(1000)
	, m_gameEnded(false)
	, m_gameWin(false)
{
	ui->setupUi(this);

	preLoadWavesInfo();
	loadTowerPositions();
	addWayPoints();

	m_audioPlayer = new AudioPlayer(this);
	m_audioPlayer->startBGM();

	QTimer *timer = new QTimer(this);
	connect(timer, SIGNAL(timeout()), this, SLOT(updateMap()));
	timer->start(30);
    //Here is Movement Function  30ms refresh once


    // Start Time - Now is 300
    //QTimer::singleShot(9999, this, SLOT(gameStart()));
}

MainWindow::~MainWindow()
{
	delete ui;
}

void MainWindow::loadTowerPositions()
{
	QFile file(":/config/TowersPosition.plist");
	if (!file.open(QFile::ReadOnly | QFile::Text))
	{
		QMessageBox::warning(this, "TowerDefense", "Cannot Open TowersPosition.plist");
		return;
	}

	PListReader reader;
	reader.read(&file);

	QList<QVariant> array = reader.data();
	foreach (QVariant dict, array)
	{
		QMap<QString, QVariant> point = dict.toMap();
		int x = point.value("x").toInt();
		int y = point.value("y").toInt();
		m_towerPositionsList.push_back(QPoint(x, y));
	}

	file.close();
}

void MainWindow::paintEvent(QPaintEvent *)
{
	if (m_gameEnded || m_gameWin)
	{
        //QString text = m_gameEnded ? "YOU LOST!!!" : "YOU WIN!!!";
		QPainter painter(this);
        //painter.setPen(QPen(Qt::red));
        //painter.drawText(rect(), Qt::AlignCenter, text);
        //return;
        QImage Win=QImage("://image/prefix/Win.png");
        QImage Fail=QImage("://image/prefix/Fail.png");
        if (m_gameEnded)
            {painter.drawImage(0,0,Fail);}
        else
            {painter.drawImage(0,0,Win);}

        return;
	}

    QPixmap cachePix(":/image/map.jpg");
	QPainter cachePainter(&cachePix);

	foreach (const TowerPosition &towerPos, m_towerPositionsList)
		towerPos.draw(&cachePainter);

	foreach (const Tower *tower, m_towersList)
		tower->draw(&cachePainter);

	foreach (const WayPoint *wayPoint, m_wayPointsList)
		wayPoint->draw(&cachePainter);

	foreach (const Enemy *enemy, m_enemyList)
		enemy->draw(&cachePainter);

	foreach (const Bullet *bullet, m_bulletList)
		bullet->draw(&cachePainter);

	drawWave(&cachePainter);
	drawHP(&cachePainter);
	drawPlayerGold(&cachePainter);

	QPainter painter(this);
	painter.drawPixmap(0, 0, cachePix);
}

void MainWindow::mousePressEvent(QMouseEvent *event)
{
	QPoint pressPos = event->pos();
	auto it = m_towerPositionsList.begin();
	while (it != m_towerPositionsList.end())
	{
		if (canBuyTower() && it->containPoint(pressPos) && !it->hasTower())
		{
			m_audioPlayer->playSound(TowerPlaceSound);
			m_playrGold -= TowerCost;
			it->setHasTower();

			Tower *tower = new Tower(it->centerPos(), this);
			m_towersList.push_back(tower);
			update();
			break;
		}

		++it;
	}
}

bool MainWindow::canBuyTower() const
{
	if (m_playrGold >= TowerCost)
		return true;
	return false;
}

void MainWindow::drawWave(QPainter *painter)
{
	painter->setPen(QPen(Qt::red));
	painter->drawText(QRect(400, 5, 100, 25), QString("WAVE : %1").arg(m_waves + 1));
}

void MainWindow::drawHP(QPainter *painter)
{
	painter->setPen(QPen(Qt::red));
	painter->drawText(QRect(30, 5, 100, 25), QString("HP : %1").arg(m_playerHp));
}

void MainWindow::drawPlayerGold(QPainter *painter)
{
	painter->setPen(QPen(Qt::red));
	painter->drawText(QRect(200, 5, 200, 25), QString("GOLD : %1").arg(m_playrGold));
}

void MainWindow::doGameOver()
{
	if (!m_gameEnded)
	{
		m_gameEnded = true;
        // End Game

	}
}

void MainWindow::awardGold(int gold)
{
	m_playrGold += gold;
	update();
}

AudioPlayer *MainWindow::audioPlayer() const
{
	return m_audioPlayer;
}

//这里用一个小封装的WayPoint来存储节点，WayPoint的行为是一个逆序链表，第一点其实是终点

void MainWindow::addWayPoints()
{
    WayPoint *wayPoint1 = new WayPoint(QPoint(760, 300));
	m_wayPointsList.push_back(wayPoint1);

    WayPoint *wayPoint2 = new WayPoint(QPoint(654, 314));
	m_wayPointsList.push_back(wayPoint2);
	wayPoint2->setNextWayPoint(wayPoint1);

    WayPoint *wayPoint3 = new WayPoint(QPoint(647, 224));
	m_wayPointsList.push_back(wayPoint3);
	wayPoint3->setNextWayPoint(wayPoint2);

    WayPoint *wayPoint4 = new WayPoint(QPoint(502, 227));
	m_wayPointsList.push_back(wayPoint4);
	wayPoint4->setNextWayPoint(wayPoint3);

    WayPoint *BackH1 = new WayPoint(QPoint(367, 225));
    m_wayPointsList.push_back(BackH1);
    BackH1->setNextWayPoint(wayPoint4);

    WayPoint *H1 = new WayPoint(QPoint(361, 132));//1st Dining Hall
    m_wayPointsList.push_back(H1);
    H1->setNextWayPoint(BackH1);

    WayPoint *wayPoint5 = new WayPoint(QPoint(367, 225));
	m_wayPointsList.push_back(wayPoint5);
    wayPoint5->setNextWayPoint(H1);

    WayPoint *wayPointN6 = new WayPoint(QPoint(297, 238)); //New Insert point
    m_wayPointsList.push_back(wayPointN6);
    wayPointN6->setNextWayPoint(wayPoint5);

    WayPoint *wayPointN5 = new WayPoint(QPoint(256, 263)); //New Insert point
    m_wayPointsList.push_back(wayPointN5);
    wayPointN5->setNextWayPoint(wayPointN6);

    WayPoint *BackH2 = new WayPoint(QPoint(187, 264));
    m_wayPointsList.push_back(BackH2);
    BackH2->setNextWayPoint(wayPointN5);

    WayPoint *H2 = new WayPoint(QPoint(187, 224));//2nd Dining Hall
    m_wayPointsList.push_back(H2);
    H2->setNextWayPoint(BackH2);

    WayPoint *NH2 = new WayPoint(QPoint(187, 264));//2nd DiningHall's Door
    m_wayPointsList.push_back(NH2);
    NH2->setNextWayPoint(H2);

    WayPoint *wayPoint6 = new WayPoint(QPoint(85, 258));
	m_wayPointsList.push_back(wayPoint6);
    wayPoint6->setNextWayPoint(NH2);

    WayPoint *wayPoint7 = new WayPoint(QPoint(85, 408));
    m_wayPointsList.push_back(wayPoint7);
    wayPoint7->setNextWayPoint(wayPoint6);

    WayPoint *wayPoint8 = new WayPoint(QPoint(138, 410));
    m_wayPointsList.push_back(wayPoint8);
    wayPoint8->setNextWayPoint(wayPoint7);

    WayPoint *wayPoint9 = new WayPoint(QPoint(275, 475));
    m_wayPointsList.push_back(wayPoint9);
    wayPoint9->setNextWayPoint(wayPoint8);

    WayPoint *backH3 = new WayPoint(QPoint(380, 475));
    m_wayPointsList.push_back(backH3);
    backH3->setNextWayPoint(wayPoint9);

    WayPoint *H3 = new WayPoint(QPoint(380, 500));//3rd DiningHall's Door
    m_wayPointsList.push_back(H3);
    H3->setNextWayPoint(backH3);

    WayPoint *NH3 = new WayPoint(QPoint(380, 475));//3rd DiningHall's Door
    m_wayPointsList.push_back(NH3);
    NH3->setNextWayPoint(H3);

    WayPoint *wayPoint10 = new WayPoint(QPoint(500, 475));
    m_wayPointsList.push_back(wayPoint10);
    wayPoint10->setNextWayPoint(NH3);

    WayPoint *wayPointN4 = new WayPoint(QPoint(507, 421)); //New Insert point
    m_wayPointsList.push_back(wayPointN4);
    wayPointN4->setNextWayPoint(wayPoint10);

    WayPoint *wayPointN3 = new WayPoint(QPoint(482, 392)); //New Insert point
    m_wayPointsList.push_back(wayPointN3);
    wayPointN3->setNextWayPoint(wayPointN4);

    WayPoint *wayPointN2 = new WayPoint(QPoint(484, 282)); //New Insert point
    m_wayPointsList.push_back(wayPointN2);
    wayPointN2->setNextWayPoint(wayPointN3);

    WayPoint *wayPointN1 = new WayPoint(QPoint(500, 232)); //New Insert point
    m_wayPointsList.push_back(wayPointN1);
    wayPointN1->setNextWayPoint(wayPointN2);

    WayPoint *wayPoint11 = new WayPoint(QPoint(500, 50));
    m_wayPointsList.push_back(wayPoint11);
    wayPoint11->setNextWayPoint(wayPointN1);
}

void MainWindow::getHpDamage(int damage/* = 1*/)
{
	m_audioPlayer->playSound(LifeLoseSound);
	m_playerHp -= damage;
	if (m_playerHp <= 0)
		doGameOver();
}

void MainWindow::removedEnemy(Enemy *enemy)
{
	Q_ASSERT(enemy);

	m_enemyList.removeOne(enemy);
	delete enemy;

	if (m_enemyList.empty())
	{
		++m_waves;
		if (!loadWave())
		{
			m_gameWin = true;
			// 游戏胜利转到游戏胜利场景
			// 这里暂时以打印处理
		}
	}
}

void MainWindow::removedBullet(Bullet *bullet)
{
	Q_ASSERT(bullet);

	m_bulletList.removeOne(bullet);
	delete bullet;
}

void MainWindow::addBullet(Bullet *bullet)
{
	Q_ASSERT(bullet);

	m_bulletList.push_back(bullet);
}

void MainWindow::updateMap()
{
	foreach (Enemy *enemy, m_enemyList)
		enemy->move();
	foreach (Tower *tower, m_towersList)
		tower->checkEnemyInRange();
	update();
}

void MainWindow::preLoadWavesInfo()
{
	QFile file(":/config/Waves.plist");
	if (!file.open(QFile::ReadOnly | QFile::Text))
	{
		QMessageBox::warning(this, "TowerDefense", "Cannot Open TowersPosition.plist");
		return;
	}

	PListReader reader;
	reader.read(&file);

	// 获取波数信息
	m_wavesInfo = reader.data();

	file.close();
}

bool MainWindow::loadWave()
{
	if (m_waves >= m_wavesInfo.size())
		return false;

	WayPoint *startWayPoint = m_wayPointsList.back();
	QList<QVariant> curWavesInfo = m_wavesInfo[m_waves].toList();

	for (int i = 0; i < curWavesInfo.size(); ++i)
	{
		QMap<QString, QVariant> dict = curWavesInfo[i].toMap();
		int spawnTime = dict.value("spawnTime").toInt();

		Enemy *enemy = new Enemy(startWayPoint, this);
		m_enemyList.push_back(enemy);
		QTimer::singleShot(spawnTime, enemy, SLOT(doActivate()));
	}

	return true;
}

QList<Enemy *> MainWindow::enemyList() const
{
	return m_enemyList;
}

void MainWindow::gameStart()
{
	loadWave();
}

void MainWindow::on_pushButton_clicked()
{
    gameStart();
    ui->pushButton->hide();
}

void MainWindow::on_pushButton_2_clicked()
{
    About_us abu;
    abu.exec();
}
