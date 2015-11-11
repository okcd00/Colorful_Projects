#include "enemy.h"
#include "waypoint.h"
#include "tower.h"
#include "utility.h"
#include "mainwindow.h"
#include "audioplayer.h"
#include <QPainter>
#include <QColor>
#include <QDebug>
#include <QMatrix>
#include <QVector2D>
#include <QtMath>

static const int Health_Bar_Width = 20;

const QSize Enemy::ms_fixedSize(52, 52);

Enemy::Enemy(WayPoint *startWayPoint, MainWindow *game,const QPixmap &sprite)
	: QObject(0)
	, m_active(false)
    , m_maxHp(66)
    , m_currentHp(66)
    , m_walkingSpeed(1.1)
	, m_rotationSprite(0.0)
	, m_pos(startWayPoint->pos())
	, m_destinationWayPoint(startWayPoint->nextWayPoint())
	, m_game(game)
	, m_sprite(sprite)
{
   // if     (w.getwaves()==2) Enemy.m_sprite = QPixmap("://image/pic/mst2.png");
   // else if(w.getwaves()==3) Enemy.m_sprite = QPixmap("://image/pic/mst3.png");
   // else if(w.getwaves()==4) Enemy.m_sprite = QPixmap("://image/pic/mst4.png");
   // else if(w.getwaves()==5) Enemy.m_sprite = QPixmap("://image/pic/mst5.png");
}

Enemy::~Enemy()
{
	m_attackedTowersList.clear();
	m_destinationWayPoint = NULL;
	m_game = NULL;
}

void Enemy::doActivate()
{
	m_active = true;
}

void Enemy::move()
{
	if (!m_active)
		return;

	if (collisionWithCircle(m_pos, 1, m_destinationWayPoint->pos(), 1))
	{
        // Reach one Point
		if (m_destinationWayPoint->nextWayPoint())
		{
            // If there's another point
			m_pos = m_destinationWayPoint->pos();
			m_destinationWayPoint = m_destinationWayPoint->nextWayPoint();
		}
		else
		{
            // Enter Final Point
			m_game->getHpDamage();
			m_game->removedEnemy(this);
			return;
		}
	}

    // When at Road
    // Destination Point Address
	QPoint targetPoint = m_destinationWayPoint->pos();
    // Can Change Speed

    // Vector normalizing
	qreal movementSpeed = m_walkingSpeed;
	QVector2D normalized(targetPoint - m_pos);
	normalized.normalize();
	m_pos = m_pos + normalized.toPoint() * movementSpeed;

    // Enemy direction
    // Adjust Beginning direction
	m_rotationSprite = qRadiansToDegrees(qAtan2(normalized.y(), normalized.x())) + 180;
}

void Enemy::draw(QPainter *painter) const
{
	if (!m_active)
		return;

	painter->save();

	QPoint healthBarPoint = m_pos + QPoint(-Health_Bar_Width / 2 - 5, -ms_fixedSize.height() / 3);
    // Paint HP
	painter->setPen(Qt::NoPen);
	painter->setBrush(Qt::red);
	QRect healthBarBackRect(healthBarPoint, QSize(Health_Bar_Width, 2));
	painter->drawRect(healthBarBackRect);

	painter->setBrush(Qt::green);
	QRect healthBarRect(healthBarPoint, QSize((double)m_currentHp / m_maxHp * Health_Bar_Width, 2));
	painter->drawRect(healthBarRect);

	// 绘制偏转坐标,由中心+偏移=左上
	static const QPoint offsetPoint(-ms_fixedSize.width() / 2, -ms_fixedSize.height() / 2);
	painter->translate(m_pos);
	painter->rotate(m_rotationSprite);
    // Paint Enemy
	painter->drawPixmap(offsetPoint, m_sprite);

	painter->restore();
}

void Enemy::getRemoved()
{
	if (m_attackedTowersList.empty())
		return;

	foreach (Tower *attacker, m_attackedTowersList)
		attacker->targetKilled();
    // Enemy Death Call
	m_game->removedEnemy(this);
}

void Enemy::getDamage(int damage)
{
	m_game->audioPlayer()->playSound(LaserShootSound);
	m_currentHp -= damage;

    // Death&Delete
	if (m_currentHp <= 0)
	{
		m_game->audioPlayer()->playSound(EnemyDestorySound);
		m_game->awardGold(200);
		getRemoved();
	}
}

void Enemy::getAttacked(Tower *attacker)
{
	m_attackedTowersList.push_back(attacker);
}

// Show Enemy Out of Range
void Enemy::gotLostSight(Tower *attacker)
{
	m_attackedTowersList.removeOne(attacker);
}

QPoint Enemy::pos() const
{
	return m_pos;
}
