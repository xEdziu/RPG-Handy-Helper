```toml
name = 'Modyfikuj grę'
description = 'Modyfikuje wskazaną grę'
method = 'PUT'
url = 'http://localhost:8888/api/v1/authorized/game/updateGame/1'
sortWeight = 4000000
id = '73a53a58-7b67-42d4-b8cd-fc9e1a45452c'

[body]
type = 'JSON'
raw = '''
{
  "name": "Example Game 2",
  "description": "This is an example game description after edit.",
  "gameMaster": {
    id: 2
  }
}'''
```
