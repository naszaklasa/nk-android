Przykładowa aplikacja na Android zintegrowana z nk.pl
========================================================
Zamieszczony tutaj kod pozwoli Ci na stworzenie aplikacji mobilnej, zintegrowanej z kontem NK. Dzięki temu, możliwe będzie logowanie się użytkowników za pomocą konta NK oraz uzyskanie dostępu do ich danych.

Mechanizm bazuje na standardzie OAuth 2, o którym więcej dowiesz się na stronie [http://tools.ietf.org/html/draft-ietf-oauth-v2-27](http://tools.ietf.org/html/draft-ietf-oauth-v2-27).

Definiowanie aplikacji
----------------------
Aby rozpocząć, należy zalogować się na [NK Developers](http://developers.nk.pl/developers). Jeżeli nie masz tam konta, wystarczy że użyjesz przycisku **Zaloguj się z NK** - zostanie ono automatycznie założone przy użyciu Twojego konta z nk.pl.

Aplikacje typu OAuth 2, z racji tego, że najczęściej są stronami www, nazywają się właśnie **Stronami**. Znajdź sekcję o tej nazwę w menu po lewej i kliknij w **Nowa strona**. W przypadku aplikacji mobilnej, jako *Redirect URI* możesz podać _http://localhost_.

Po stworzeniu Strony będziesz mógł odczytać wygenerowany dla niej *key* i *secret*, które należy użyć w kodzie aplikacji.

Więcej informacji znajdziesz [tutaj](http://developers.nk.pl/pl/dokumentacja/tworcy-stron-internetowych/integracja-z-api/).

Konfigurowanie projektu aplikacji
---------------------------------

Źródła zawierają gotowy projekt do zaimportowania w [Eclipse](http://www.eclipse.org/) wraz z [Android SDK](http://developer.android.com/sdk/index.html).

W pliku _NKExample.java_ należy podmienić _KEY_ i _SECRET_ na właściwy dla swojej aplikacji.

Ważne, aby w _AdnroidManifest.xml_ znajdowały się odpowiednie uprawnienia:

`<uses-permission android:name="android.permission.INTERNET" />`

Uruchomienie
------------

Odpalmy aplikację, klikamy przycisk "Zaloguj się z NK" - zaloguj się używając danych z nk.pl, udziel pozwoleń dla aplikacji. Kliknij "Pobierz dane", aby pobrać dane.

![Pan Gąbka vs Android](http://developers.nk.pl/wp-content/uploads/PanGabka_Android.png)
