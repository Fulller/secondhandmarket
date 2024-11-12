

const toggleButtons = document.querySelectorAll('[data-modal-toggle]');
const hideButtons = document.querySelectorAll('[data-modal-hide]');
const modal = document.getElementById('popup-modal');

// Mở modal khi bấm vào nút mở
document.querySelectorAll('[data-modal-target]').forEach(button => {
    button.addEventListener('click', () => {
        const targetId = button.getAttribute('data-modal-target');
        const modal = document.getElementById(targetId);
        if (modal) {
            modal.classList.remove('hidden');
        }
    });
});

// Đóng modal khi bấm vào nút "Hủy" hoặc dấu "X"
document.querySelectorAll('[data-modal-hide]').forEach(button => {
    button.addEventListener('click', () => {
        const targetId = button.getAttribute('data-modal-hide');
        const modal = document.getElementById(targetId);
        if (modal) {
            modal.classList.add('hidden');
        }
    });
});

// Đóng modal khi bấm vào vùng ngoài modal (nếu cần)
document.querySelectorAll('.modal-background').forEach(background => {
    background.addEventListener('click', (event) => {
        const modal = event.target.closest('.modal');
        if (modal) {
            modal.classList.add('hidden');
        }
    });
});



