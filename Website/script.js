// GeoSetu Website JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize the website
    initializeWebsite();
    
    // Add interactive elements
    addInteractiveEffects();
    
    // Add app button functionality
    setupAppButton();
    
    // Add parallax effect to background elements
    addParallaxEffect();
    
    // Add smooth animations
    addSmoothAnimations();
});

function initializeWebsite() {
    console.log('GeoSetu website loaded successfully!');
    
    // Add loading animation to elements
    const elements = document.querySelectorAll('.logo, .brand-name, .description, .app-button');
    elements.forEach((element, index) => {
        element.style.animationDelay = `${index * 0.2}s`;
        element.classList.add('fade-in-element');
    });
}

function addInteractiveEffects() {
    const logo = document.getElementById('logo');
    const brandName = document.getElementById('brandName');
    
    // Add click effect to logo
    logo.addEventListener('click', function() {
        this.style.transform = 'scale(1.2) rotate(360deg)';
        setTimeout(() => {
            this.style.transform = 'scale(1) rotate(0deg)';
        }, 600);
    });
    
    // Add hover effect to brand name
    brandName.addEventListener('mouseenter', function() {
        this.style.filter = 'hue-rotate(30deg) brightness(1.1)';
    });
    
    brandName.addEventListener('mouseleave', function() {
        this.style.filter = 'drop-shadow(0 5px 15px rgba(0, 0, 0, 0.1))';
    });
    
    // Add description text animation
    const description = document.querySelector('.description p');
    let originalText = description.textContent;
    
    description.addEventListener('mouseenter', function() {
        this.style.color = '#2563eb';
        this.style.transform = 'scale(1.02)';
    });
    
    description.addEventListener('mouseleave', function() {
        this.style.color = '#333';
        this.style.transform = 'scale(1)';
    });
}

function setupAppButton() {
    const appButton = document.getElementById('appButton');
    
    appButton.addEventListener('click', function(e) {
        e.preventDefault();
        // Add click animation
        this.style.transform = 'scale(0.95)';
        // Replace with your actual S3 download link
        const s3Url = this.getAttribute('data-download-url') || 'https://geosetu-apk-download.s3.eu-north-1.amazonaws.com/GeoSetu.apk';
        // Create a temporary anchor to trigger download
        const a = document.createElement('a');
        a.href = s3Url;
        a.setAttribute('download', '');
        a.style.display = 'none';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        setTimeout(() => {
            this.style.transform = 'translateY(-3px)';
        }, 150);
    });
    
    // Add ripple effect on click
    appButton.addEventListener('click', function(e) {
        const ripple = document.createElement('span');
        const rect = this.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;
        
        ripple.style.cssText = `
            position: absolute;
            width: ${size}px;
            height: ${size}px;
            left: ${x}px;
            top: ${y}px;
            background: rgba(255, 255, 255, 0.5);
            border-radius: 50%;
            transform: scale(0);
            animation: ripple 0.6s ease-out;
            pointer-events: none;
        `;
        
        this.style.position = 'relative';
        this.style.overflow = 'hidden';
        this.appendChild(ripple);
        
        setTimeout(() => {
            ripple.remove();
        }, 600);
    });
}

// function showAppLaunchMessage() {
//     // Create a temporary message overlay
//     const overlay = document.createElement('div');
//     overlay.style.cssText = `
//         position: fixed;
//         top: 0;
//         left: 0;
//         width: 100%;
//         height: 100%;
//         background: rgba(0, 0, 0, 0.8);
//         display: flex;
//         align-items: center;
//         justify-content: center;
//         z-index: 1000;
//         opacity: 0;
//         transition: opacity 0.3s ease;
//     `;
    
//     const message = document.createElement('div');
//     message.style.cssText = `
//         background: white;
//         padding: 40px;
//         border-radius: 20px;
//         text-align: center;
//         max-width: 400px;
//         box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
//         transform: scale(0.8);
//         transition: transform 0.3s ease;
//     `;
    
//     message.innerHTML = `
//         <h2 style="color: #333; margin-bottom: 20px; font-size: 24px;">🚀 Coming Soon!</h2>
//         <p style="color: #666; line-height: 1.6; margin-bottom: 20px;">
//             The GeoSetu app is currently in development. 
//             We're working hard to bring you the bridge between technology, governance, and communities.
//         </p>
//         <button onclick="this.parentElement.parentElement.remove()" 
//                 style="background: #2563eb; color: white; border: none; padding: 12px 24px; 
//                        border-radius: 25px; cursor: pointer; font-weight: 600;">
//             Got it!
//         </button>
//     `;
    
//     overlay.appendChild(message);
//     document.body.appendChild(overlay);
    
//     setTimeout(() => {
//         overlay.style.opacity = '1';
//         message.style.transform = 'scale(1)';
//     }, 10);
    
//     // Auto-remove after 5 seconds
//     setTimeout(() => {
//         if (overlay.parentElement) {
//             overlay.style.opacity = '0';
//             setTimeout(() => overlay.remove(), 300);
//         }
//     }, 5000);
// }

function addParallaxEffect() {
    window.addEventListener('mousemove', function(e) {
        const circles = document.querySelectorAll('.circle');
        const mouseX = e.clientX / window.innerWidth;
        const mouseY = e.clientY / window.innerHeight;
        
        circles.forEach((circle, index) => {
            const speed = (index + 1) * 0.05;
            const x = (mouseX - 0.5) * speed * 100;
            const y = (mouseY - 0.5) * speed * 100;
            
            circle.style.transform = `translate(${x}px, ${y}px)`;
        });
    });
}

function addSmoothAnimations() {
    // Add CSS for ripple animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes ripple {
            to {
                transform: scale(2);
                opacity: 0;
            }
        }
        
        .fade-in-element {
            animation: fadeInUp 1s ease-out both;
        }
        
        /* Smooth transitions for all interactive elements */
        * {
            transition: all 0.3s ease;
        }
        
        /* Custom scrollbar for better UX */
        ::-webkit-scrollbar {
            width: 8px;
        }
        
        ::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, 0.1);
        }
        
        ::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.3);
            border-radius: 4px;
        }
        
        ::-webkit-scrollbar-thumb:hover {
            background: rgba(255, 255, 255, 0.5);
        }
    `;
    
    document.head.appendChild(style);
}

// Add keyboard navigation support
document.addEventListener('keydown', function(e) {
    if (e.key === 'Enter' || e.key === ' ') {
        const focusedElement = document.activeElement;
        if (focusedElement && focusedElement.classList.contains('app-button')) {
            e.preventDefault();
            focusedElement.click();
        }
    }
});

// Performance optimization: Throttle mouse move events
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

// Apply throttling to mousemove for better performance
window.addEventListener('mousemove', throttle(function(e) {
    // Mousemove logic is handled in addParallaxEffect
}, 16)); // ~60fps