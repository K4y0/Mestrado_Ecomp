import os, time
from time import gmtime, strftime
import sys

if len(sys.argv) != 2:
    print ("Please, provide the parameters to run this program")
    sys.exit(0)

# Return CPU temperature as a character string
def getCPUtemperature():
    res = os.popen('vcgencmd measure_temp').readline()
    return(res.replace("temp=","").replace("'C\n",""))

# Return RAM information (unit=kb) in a list
# Index 0: total RAM
# Index 1: used RAM
# Index 2: free RAM
def getRAMinfo():
    p = os.popen('free')
    i = 0
    while 1:
        i = i + 1
        line = p.readline()
        if i==2:
            return(line.split()[1:4])

# Return % of CPU used by user as a character string
def getCPUuse():
    return(str(os.popen("top -n1 | awk '/Cpu\(s\):/ {print $2}'").readline().strip(\
        )))

# Return information about disk space as a list (unit included)
# Index 0: total disk space
# Index 1: used disk space
# Index 2: remaining disk space
# Index 3: percentage of disk used
def getDiskSpace():
    p = os.popen("df -h /")
    i = 0
    while 1:
        i = i +1
        line = p.readline()
        if i==2:
            return(line.split()[1:5])

def write(lines, namefile):
    arq = open('files_' + namefile +'.csv', 'a')
    if lines == 1:
        arq.writelines("time;")
        arq.writelines("date;")
        arq.writelines("cpu_use;")
        arq.writelines("ram_total;")
        arq.writelines("ram_used;")
        arq.writelines("ram_free;")
        arq.writelines("disk_total;")
        arq.writelines("disk_free;")
        arq.writelines("disk_perc;")

    arq.writelines("\n%s;" % strftime("%H:%M:%S", gmtime()))

    arq.writelines("%s;" % strftime("%d/%b/%Y", gmtime()))

    arq.writelines("%s;" % getCPUuse())

    RAM_stats = getRAMinfo()

    arq.writelines("%s;" %round(int(RAM_stats[0]) / 1000,1))

    arq.writelines("%s;" % round(int(RAM_stats[1]) / 1000,1))

    arq.writelines("%s;" % round(int(RAM_stats[2]) / 1000,1))

    DISK_stats = getDiskSpace()

    arq.writelines("%s;" % DISK_stats[0])
    arq.writelines("%s;" % DISK_stats[1])
    arq.writelines("%s;" % DISK_stats[3])

    arq.close()

    print("recorded data - line " + str(lines))

lines = 1

try:
    clockname = str(sys.argv[1]) + "_h_" + strftime("%H", gmtime())
    while 1:
        write(lines, clockname)
        lines = lines + 1
        time.sleep(0.100)
except KeyboardInterrupt:
    lines =  lines - 1
    print ("The end! Inserts: " + str(lines))