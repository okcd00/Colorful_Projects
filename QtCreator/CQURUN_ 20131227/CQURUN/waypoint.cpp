#include "waypoint.h"
#include <QPainter>
#include <QColor>

WayPoint::WayPoint(QPoint pos)
	: m_pos(pos)
	, m_nextWayPoint(NULL)
{
}

void WayPoint::setNextWayPoint(WayPoint *nextPoint)
{
	m_nextWayPoint = nextPoint;
}

WayPoint* WayPoint::nextWayPoint() const
{
	return m_nextWayPoint;
}

const QPoint WayPoint::pos() const
{
	return m_pos;
}

void WayPoint::draw(QPainter *painter) const
{                               //For testing point Selection
	painter->save();
    painter->setPen(Qt::red);
    //painter->drawEllipse(m_pos, 4, 4);
    //painter->drawEllipse(m_pos, 2, 2);

    if (m_nextWayPoint)
        //painter->drawLine(m_pos, m_nextWayPoint->m_pos);
    painter->restore();
}
