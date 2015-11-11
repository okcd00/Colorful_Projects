#ifndef AUDIOPLAYER_H
#define AUDIOPLAYER_H

/*******************************************************
           Use for Play Music and audio reflect ForMac
********************************************************/
//Editby okcd00 2013.12.26 Failed

#include <QObject>

class QMediaPlayer;

enum SoundType
{
    TowerPlaceSound,		// SetTower
    LifeLoseSound,			// Be Attacked
    LaserShootSound,		// Hit Emeny
    EnemyDestorySound		// Enemy Death
};

class AudioPlayer : public QObject
{
public:
	explicit AudioPlayer(QObject *parent = 0);

	void startBGM();
	void playSound(SoundType soundType);

private:
    QMediaPlayer *m_backgroundMusic; // BGM_Set
};


#endif // AUDIOPLAYER_H
