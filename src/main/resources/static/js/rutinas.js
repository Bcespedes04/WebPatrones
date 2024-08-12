document.addEventListener('DOMContentLoaded', function() {
    console.log("Gym website loaded.");

    // Ejemplo de función para manejar el login
    function login(email, password) {
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded' // Usar este content type si envías datos como URL encoded
            },
            body: new URLSearchParams({ email: email, password: password })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                localStorage.setItem('userId', data.userId); // Guarda el userId en localStorage
                window.location.href = '/principal'; // Redirige al usuario a la página principal
            } else {
                alert(data.message || 'Error en la autenticación');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    // Ejemplo de cómo podrías llamar a la función de login
    const loginForm = document.querySelector('#loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Prevenir el envío del formulario tradicional
            const email = document.querySelector('#email').value;
            const password = document.querySelector('#password').value;
            login(email, password);
        });
    }

    // Ejemplo de cómo acceder al userId desde localStorage
    function obtenerUserId() {
        return localStorage.getItem('userId');
    }

    // Verificar si el usuario está logueado
    const userId = obtenerUserId();
    if (userId) {
        console.log('El userId es:', userId);
        // Aquí puedes agregar lógica para personalizar la experiencia del usuario logueado
    } else {
        console.log('No hay un userId almacenado, el usuario no está logueado.');
    }

    // Ejemplo de función para logout
    function logout() {
        localStorage.removeItem('userId');
        window.location.href = '/login';
    }

    // Ejemplo de cómo podrías conectar un botón de logout a la función
    const logoutButton = document.querySelector('#logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function() {
            logout();
        });
    }
});





