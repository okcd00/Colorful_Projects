# Introduction

This Program is used to control a CrossRoad TrafficLight   
There are three Switches reflecting three sensor( if_has_Person(), if_NSdir_has_car(), if_WEdir_has_car() )   
The traffic lights will change mode when States of Sensors changes.    

## FSM- 有限状态机
STyp FSM[9]=  
{     
//{OUT,OutPF,Time,{    None,	   PE0,     PE1,   PE1&0,     PE2,   PE2&0,   PE2&1, PE2&1&0}}
 {0x0C, 0x02, 100,{     goW,     goW,   waitW,   waitW,   waitW,   waitW,   waitW,   waitW}},   //goW  
 {0x14, 0x02, 60, {   waitW,     goW,     goS,     goS,    walk,    walk,     goS,     goS}},   //waitW  
 {0x21, 0x02, 100,{     goS,   waitS,     goS,   waitS,   waitS,   waitS,   waitS,   waitS}},   //goS  
 {0x22, 0x02, 60, {   waitS,     goW,     goS,     goW,    walk,    walk,    walk,    walk}},   //waitS  
 {0x24, 0x08, 100,{    walk, notWalk, notWalk, notWalk,    walk, notWalk, notWalk, notWalk}},   //walk  
 {0x24, 0x02, 60, { notWalk, walkOff, walkOff, walkOff, walkOff, walkOff, walkOff, walkOff}},   //notWalk  
 {0x24, 0x00, 60, { walkOff,notWalk2,notWalk2,notWalk2,notWalk2,notWalk2,notWalk2,notWalk2}},   //walkOff  
 {0x24, 0x02, 60, {notWalk2,walkOff2,walkOff2,walkOff2,walkOff2,walkOff2,walkOff2,walkOff2}},   //notWalk2  
 {0x24, 0x00, 60, {walkOff2,     goW,     goS,     goW,    walk,     goW,     goS,     goW}}    //walkOff2  
};  
