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
       //Buffer data 
        ByteBuffer  bf = redInIOData() /*read data in io channel*/

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
          
            //TODO  do yours  business
        }
    } catch (Exception e) {
        LOGGER.error("解析包出现异常",e);
    }

```


### Parser Server give out to Client data package
```java
    //Buffer data 
    ByteBuffer  bf = redInIOData() /*read data in io channel*/

    ServerDatagramParser datagramParser = new ServerDatagramParser()
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
                //TODO  do yours  business
            }
        } catch (Exception e) {
            LOGGER.error("解析包出现异常",e);
        }

```