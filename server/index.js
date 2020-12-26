const { group } = require('console');

var socketIO = require('socket.io'),
    http = require('http'),
    port = process.env.PORT || 4000,
    ip = process.env.IP || '192.168.0.103',
    server = http.createServer().listen(port, ip, function() {
        console.log("IP = " , ip);
        console.log("port = " , port);
        console.log("start socket successfully");
});


var usersList=[];
var groupList = [];

io = socketIO.listen(server);
//io.set('match origin protocol', true);
io.set('origins', '*:*');

var run = function(socket){

socket.on('join', function(user){
    for (let i = 0; i < usersList.length; i++) { 
        if (usersList[i]['id']==user['id']) {
           io.emit('join',false);
            break;
        }else if(i==usersList.length-1){
            usersList.push(user);
           io.emit('join',true);
           io.emit('AllUsers', usersList);    
        }
      }
      if(usersList.length==0){
        usersList.push(user);
        io.emit('join',true);
        io.emit('AllUsers', usersList);
     }
   console.log('new user add=>',user);
 });


 socket.on('updateStatus', function(User){
    console.log("updateStatus"+User)
    for (let i = 0; i < usersList.length; i++) { 
      if (usersList[i]['id']==User['id']) {
        usersList[i]['isOnline']=User['isOnline'];
         io.emit('AllUsers', usersList);
          break;
      }
    }
});

socket.on('AllUsers', function(user){
    io.emit('AllUsers',usersList);
  });



    socket.on("message", function(value) {
        console.log(value);
        io.emit('message',value);
    });


    socket.on('Group', function(Group){
        groupList.push(Group)
       io.emit('Group',Group);
       console.log('new Group add=>',Group);
       io.emit('AllGroup',groupList);
     });
     
      socket.on('AllGroup', function(Group){
       io.emit('AllGroup',groupList);
     });
     
     socket.on('UpdateProfile', function(User){
        console.log("UpdateProfile"+User)
        for (let i = 0; i < usersList.length; i++) { 
          if (usersList[i]['id']==User['id']) {
            usersList[i]['image']=User['image'];
            usersList[i]['username']=User['username'];
             io.emit('AllUsers', usersList);
              break;
          }
        }
    });
  
}

io.sockets.on('connection', run);