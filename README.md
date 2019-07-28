# Vanitas

This project contains PostgreSQL interaction protocol implement
,support PostgreSQL interaction protocol V3.0 ,be equivalent to PostgreSQL 7.4 + 


This project provides the following features:



- [x] Login with username/password
- [x] Execution of simple SQL Query
- [ ] Execution of prepared statements with bindings 
- [ ] Support text/binary result
- [ ] Support SSL/TLS connection
- [ ] Postgres  replication

## Usage

### Parser Client give out to Server data package

```java

  FrontDatagramParser datagramParser = new FrontDatagramParser();
  
  ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  
  try {
        buffer.write(bf.array());
        byte[] packetDatas = buffer.toByteArray();
        buffer.reset();
        DatagramFrames frames = datagramParser.split(ByteBuffer.wrap(packetDatas), 0);
        // 剩余的半包
        if(frames.getOffcut()!= null){
            buffer.write(frames.getOffcut());
        }
        for (byte[] frame : frames.getFrames()) {
            Datagram datagram = datagramParser.parse(frame);
            LOGGER.error("Datagram:{}",datagram);
            doProcess(datagram,source); //每个包处理一个 
        }
    } catch (Exception e) {
        LOGGER.error("解析包出现异常",e);
    }

```