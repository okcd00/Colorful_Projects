#Introduction

>This Program is used to control 3LED lights with 2Switches.

*** Restricted Version with 32768 Byte Code Size Limit
*** Currently used: 9820 Bytes (29%)

WS 1, `TExaS_Ports
LA ((PORTF & 0x00000010) >> 4 & 0x10) >> 4
LA ((PORTF & 0x00000004) >> 2 & 0x4) >> 2

Start grading Lab 6
Clock rate appears to be : 80 MHz
Running 3 tests

0) Initialization tests :
 - Verifying Lab 6 input PF4 configuration...
   Pass: PORTF DEN bit 4 is high
   Pass: PORTF PUR bit 4 is high
   Pass: PORTF DIR bit 4 is low
   Pass: PORTF AFSEL bit 4 is low
   Pass: PORTF AMSEL bits 4 are low
   Pass: PORTF PCTL bits 19-16 are low
 - Verifying Lab 6 output (PF3,2,1) configuration...
   Pass: PORTF DEN bit PORTF is high
   Pass: PORTF DIR bit PORTF is high
   Pass: PORTF AFSEL bit PORTF is low
   Pass: PORTF AMSEL bit PORTF are low
   Pass: PORTF PCTL bits 11-8 are low
 - Verifying PLL configuration...
   Pass: RCC XTAL bits are configured for 16 MHz XTAL (0x540)
   Pass: RCC2 OSCSRC2 bits are zero
   Pass: RCC2 PWRDN2 bit is low
   Pass: RCC2 DIV400 bit is high
   Pass: RCC2 USERCC2 bit is high
   Pass: RCC2 SYSDIV2 bits are 4
1) Switch not pressed test :
 - Test PASSED

2) Switch pressed test :
 - Test PASSED

Done Lab 6 grading. Score is 100
