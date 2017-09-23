# xiaomi.vacuum
simple spring boot app to provide rest API to call Xiaomi Vacuum cleaner python commands on linux.

Run by:
java -jar xiaomi.server.jar --xiaomi.ip=192.168.1.xx --xiaomi.token=xxxxxx

Call endpoint by:
http://localhost:8090/controller/{command}

where command:
1) start
2) stop
3) home
4) fanspeed integer

User python library to order Xiaomi:
https://github.com/rytilahti/python-mirobo
