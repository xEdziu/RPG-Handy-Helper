```toml
name = 'Modyfikuj role użytkownika w grze'
description = 'Zmienia rolę użytkownika w grze'
method = 'PUT'
url = 'http://localhost:8888/api/v1/authorized/game/updateGameUserRole/1'
sortWeight = 5000000
id = '9cda84ef-4a25-44f5-9c88-67e0669fb0ba'

[body]
type = 'JSON'
raw = '''
{ 
  "role": "SPECTATOR"
}'''
```
