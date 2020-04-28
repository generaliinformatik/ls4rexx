/*REXX*/

call TESTPROC
call "EXTREXX"
call EXTREXX2

exit 0

TESTPROC: procedure
  return

/* ignored duplicate */
testproc: procedure
  return