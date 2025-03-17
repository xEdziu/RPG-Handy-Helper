```toml
name = 'Login Admin'
description = 'loguje na admina'
method = 'POST'
url = 'http://localhost:8888/login'
sortWeight = 3000000
id = '2a438e47-9129-492b-b39a-762372dcdeb1'

[[body.urlEncoded]]
key = 'username'
value = 'zuber'

[[body.urlEncoded]]
key = 'password'
value = 'adminPassword'
```
