# java-was-2023

Java Web Application Server 2023

## step-1 기록 사항
- 헤더 파싱하는 과정에서 http 헤더의 format에 대한 예외 처리가 있어야 할 것 같다. HTTP 요청이 아닐 경우의 처리 
- concurrent package를 이용하되, 어떤 스레드풀 객체를 이용할 것인지에 대해서 고민했다. 작업의 발생이 일정하고, 처리하는 작업의 로드도 작고 일정하기 때문에 newFixedThreadPool() 메소드를 이용했다.
- HttpReqeust, HttpResponse, URI 클래스를 VO로 만들었다. 발생하는 요청 정보를 담고, (HttpRequest) 그에 대한 응답 정보를 담는 (HttpResponse) 역할을 하기 때문에 생성 이후에는 변경을 제한하고 의미를 더할 필요가 있다고 생각했다.
- HttpResponse 객체를 OutputStream으로 파싱하고 응답을 보내는 로직을 어떤 클래스에서 맡아야할 지에 대한 고민을 했다. VO인 HttpResponse 객체가 로직을 갖고 있는 것보다, 파싱을 하는 역할과 응답을 보내는 역할을 따로 정의하는 것이 좋을 것 같다고 생각했다.
  요청을 받고 응답을 내보내는 로직은 RequestHandler에서 갖고 있는데, 더 많은 Http Method나 query와 관련된 요구사항이 발생했을 때 이를 다시 분리하고 일반화할 필요가 있을 것 같다.
- ParsingUtils -> inputStream을 입력받아 HttpRequest를 반환하거나, HttpResponse를 입력받아 Outputstream을 반환하는 로직을 모두 갖고 있게 하는 방법과 지금의 방법을 고민했다. 
  조금 더 일반적으로 쓰이는 것이 좋을 것 같아서 지금의 방법을 선택했다. -> 다시 반대로 수정했다. Parsing하는 역할을 분리하는 것만으로 충분한 것 같고, 이 로직이 일반적으로 쓰이지 않을 것이라고 생각했기 때문이다.
- ByteArrayOutputStream 객체를 단순히 데이터 이동의 수단으로 쓰는 것 -> 더 좋은 방법 생각해보기
- 정적인 템플릿 요청에 대해서, 이걸 찾아서 바이트로 넘겨주는 역할을 분리해야할 것 같다.