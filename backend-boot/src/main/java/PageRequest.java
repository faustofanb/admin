import org.springframework.http.HttpStatus;

void main() {
    hello();
}

void hello() {
    System.out.println(HttpStatus.OK.value());
}