```toml
name = 'Ustawienie ścieżki do zdjęcia'
description = 'Ustawia ścieżkę do zdjęcia użytkownika'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/user/setUserPhoto'
sortWeight = 6000000
id = '04b9e863-0f83-4591-abb3-057c175c6f24'

[body]
type = 'JSON'
raw = '''
{
  "userPhotoPath": "/path/to/photo.jpg"  
}'''
```
