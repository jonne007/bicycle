# Mill, Cask, Scala.js with shared js code between server and client
Ported from https://github.com/jrudolph/akka-http-scala-js-websocket-chat

Now using:
- [Mill build tool](https://www.lihaoyi.com/mill/)
- [Cask web server](https://www.lihaoyi.com/cask/)
- [Scala.js](https://www.scala-js.org) example client with [shared](build.sc) code between [server](server/src/Server.scala) and [client](js/src/MainJs.scala).
- Vuejs gui nuxt integrated in mill build
- VS Code Vetur configured. https://marketplace.visualstudio.com/items?itemName=octref.vetur

Currently needs initial `npm install` in vuegui folder.
Set up for development, use: `mill -w server.runBackground`

`http://localhost:8384/htm/`

Cask file serving has a nasty [hack](server/src/Server.scala) to be able to serve a default page required by vuejs routing. This would be handled by reverseproxy (eg nginx) in production scenario.




create, update and get

mill -w server.test backend.BicycleSuite    

To compile the code mill reboot.compile To run the tests mill reboot.test To continually run the tests mill -w reboot.test

bicycle store / Jonne
car shop / Fredrik
flower shop / Matte
skapa modellen (case klasser) 
skapa saker i lager 
köpa saker 
saker söka på olika kriterier - pris - namn (case insensitive)

SERVER TEST TJOSAN

mill -w reboot.runBackground        

curl -XPOST http://localhost:8080/cycles -d '{"brand":"cresent", "price":300, "stock":22}'

curl -XGET http://localhost:8080/cycles 

https://github.com/OpenResult/caskscalajs
git@github.com:Magua/myapp.git
git clone git@github.com:Magua/myapp.git

NPM INSTALL in vuejs

lös backend på Servern

Läs på vuejs
kör tutorial vue nuxtJS -- https://nuxtjs.org/tutorials/creating-a-nuxt-module - Create a blog
vuejs.org = matnyttigt junk!!!

http://localhost:8384/htm/

- använd datat från textfälten när du skapar ny cyckel
- cykel
- Lägg till ett X i sluttet på varje rad som gör delete.
- gör så att listan laddas när man kommer in på sidan direkt
- lägg till lite testdata i backend så det finns när man ska test
- uppdatera litstan när man skapar en ny