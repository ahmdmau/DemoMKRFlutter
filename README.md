# DemoMKRFlutter
This Sample Project is demonstrating how to integrating flutter module into existing android native app.

## How to Use
- Clone flutter module
- Run following command:
```sh
flutter pub get & flutter build aar
```
- Copy the maven url from terminal once you done with build aar
- Paste the maven url in build.gradle (App) then sync the gradle
- Create new file application, and on onCreate function type following code for configuring MKRAuthentication library:
```sh
MKRAuthentication.configure("YOUR_CLIENT_ID", MKRAuthenticationConfig.STAGING, this)
```
- Then add following code to run flutter module on your activity (i.e MainActivity):
```sh
MKRAuthentication.auth(this)
```
- Don't forget to add onActivityResult func on your activity that call flutter module to get the result from flutter module
```sh
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_AUTH -> {
                // do somethin
            }
            REQUEST_CODE_REFRESH -> {
                // do somethin
            }
        }
    }
```
