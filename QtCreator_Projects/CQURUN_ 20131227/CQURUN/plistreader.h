#ifndef PLISTREADER_H
#define PLISTREADER_H
/********************************************************
     Use For Read Plist File to get statics (From CSDN)
*********************************************************/
#include <QXmlStreamReader>

class PListReader
{
public:
	PListReader();

	bool read(QIODevice *device);
	const QList<QVariant> data() const;

	QString errorString() const;

private:
	void readPList();
	void readArray(QList<QVariant> &array);
	void readDict(QList<QVariant> &array);
	void readKey(QMap<QString, QVariant> &dict);

private:
	QXmlStreamReader	m_xmlReader;
	QList<QVariant>		m_data;
};

#endif // PLISTREADER_H
