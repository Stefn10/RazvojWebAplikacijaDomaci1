# Pre odbrane

## Koraci
- [ ] biblioteka za JSON
---
- [x] Razumeti helper-server Main klasu

- [ ] Fokusiraj se ovim redom:

6. **`helper-server` util klase**

    * `HttpResponseWriter`
    * `HttpRequestParser`
    * `HelperRepository`
    * Ovo radi tek kad odlučiš da `helper-server/Main` razbiješ na lepše delove.
    * Trenutno su manje prioritetni od `Main` klase.

7. **Tek posle toga eventualni redo delova koji su već “explained”**

    * Za sada ih ne diraj ako već rade ili su makar dovoljno jasni da se nastavi dalje.

**Najkraće:**

* prvo **oba `Main`**
* pa **`QuotesPageServlet`**
* pa **`AppContainer`**
* pa **`HtmlRenderer`**
* pa tek onda pomoćne util klase

**Zašto baš tako**

* Zato što zadatak suštinski traži:

    * pomoćni servis koji vraća QOD,
    * glavni servis koji prikazuje stranu i čuva citate,
    * komunikaciju između njih preko HTTP/socket-a.
* Sve ostalo je podrška toj osnovnoj putanji.


###