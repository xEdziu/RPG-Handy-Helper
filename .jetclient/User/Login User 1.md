```toml
name = 'Login User 1'
description = 'loguje na usera 1'
method = 'POST'
url = 'http://localhost:8888/login'
sortWeight = 4000000
id = 'b1fa2120-3740-441a-9df0-d762924d5d8c'

[[body.urlEncoded]]
key = 'username'
value = 'user1'

[[body.urlEncoded]]
key = 'password'
value = 'password123'
```
