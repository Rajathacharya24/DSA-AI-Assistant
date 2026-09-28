const API_BASE = '/api';

// State
let currentUser = null;
let token = localStorage.getItem('dsa_token');
let userId = localStorage.getItem('dsa_user_id');
let currentView = 'login-view';

// DOM Elements
const views = document.querySelectorAll('.view');
const navLinks = document.querySelectorAll('.nav-link');
const logoutBtn = document.getElementById('logout-btn');
const toast = document.getElementById('toast');

// Forms
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const chatForm = document.getElementById('chat-form');

// Initialization
function init() {
    setupNavigation();
    setupForms();
    checkAuth();
}

// Navigation & Auth State
function setupNavigation() {
    navLinks.forEach(link => {
        if (link.id !== 'logout-btn') {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const target = e.target.getAttribute('data-target');
                switchView(target);
            });
        }
    });

    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();
        logout();
    });
}

function switchView(viewId) {
    views.forEach(view => view.classList.remove('active'));
    document.getElementById(viewId).classList.add('active');
    
    navLinks.forEach(link => link.classList.remove('active'));
    const activeLink = document.querySelector(`[data-target="${viewId}"]`);
    if(activeLink) activeLink.classList.add('active');

    currentView = viewId;

    // Load data based on view
    if (viewId === 'dashboard-view') loadDashboard();
    if (viewId === 'problems-view') loadProblems();
    if (viewId === 'progress-view') loadProgress();
}

function checkAuth() {
    if (token && userId) {
        document.body.classList.add('logged-in');
        document.querySelectorAll('.public-only').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.auth-only').forEach(el => el.style.display = 'inline-block');
        switchView('dashboard-view');
    } else {
        document.body.classList.remove('logged-in');
        document.querySelectorAll('.public-only').forEach(el => el.style.display = 'inline-block');
        document.querySelectorAll('.auth-only').forEach(el => el.style.display = 'none');
        switchView('login-view');
    }
}

function logout() {
    localStorage.removeItem('dsa_token');
    localStorage.removeItem('dsa_user_id');
    token = null;
    userId = null;
    showToast('Logged out successfully', 'success');
    checkAuth();
}

// API Helpers
async function apiCall(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`;
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(url, { ...options, headers });
        const isJson = response.headers.get('content-type')?.includes('application/json');
        const data = isJson ? await response.json() : await response.text();

        if (!response.ok) {
            if (response.status === 401) {
                logout();
                throw new Error('Session expired. Please login again.');
            }
            throw new Error(data.message || data.error || 'API Request Failed');
        }

        return data;
    } catch (error) {
        showToast(error.message, 'error');
        throw error;
    }
}

function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast ${type}`;
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

// Forms Setup
function setupForms() {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;
        const btn = e.target.querySelector('button');
        
        try {
            btn.disabled = true;
            btn.textContent = 'Logging in...';
            const data = await apiCall('/auth/login', {
                method: 'POST',
                body: JSON.stringify({ username, password })
            });
            token = data.token;
            userId = data.userId;
            localStorage.setItem('dsa_token', token);
            localStorage.setItem('dsa_user_id', userId);
            showToast('Login successful', 'success');
            checkAuth();
            loginForm.reset();
        } catch (err) {
            // Error handled by apiCall
        } finally {
            btn.disabled = false;
            btn.textContent = 'Login';
        }
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('register-username').value;
        const email = document.getElementById('register-email').value;
        const password = document.getElementById('register-password').value;
        const btn = e.target.querySelector('button');
        
        try {
            btn.disabled = true;
            btn.textContent = 'Registering...';
            const data = await apiCall('/auth/register', {
                method: 'POST',
                body: JSON.stringify({ username, email, password })
            });
            token = data.token;
            userId = data.userId;
            localStorage.setItem('dsa_token', token);
            localStorage.setItem('dsa_user_id', userId);
            showToast('Registration successful', 'success');
            checkAuth();
            registerForm.reset();
        } catch (err) {
            // Error handled by apiCall
        } finally {
            btn.disabled = false;
            btn.textContent = 'Register';
        }
    });

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const input = document.getElementById('chat-input');
        const message = input.value.trim();
        if (!message) return;

        input.value = '';
        addChatMessage(message, 'user');
        
        const history = document.getElementById('chat-history');
        const loadingId = 'loading-' + Date.now();
        history.insertAdjacentHTML('beforeend', `
            <div class="message ai-message" id="${loadingId}">
                <div class="message-content">
                    <div class="loading">
                        <div class="loading-dot"></div>
                        <div class="loading-dot"></div>
                        <div class="loading-dot"></div>
                    </div>
                </div>
            </div>
        `);
        history.scrollTop = history.scrollHeight;

        try {
            const data = await apiCall('/chat', {
                method: 'POST',
                body: JSON.stringify({ message })
            });
            document.getElementById(loadingId).remove();
            addChatMessage(data.response || data, 'ai'); // fallback for string response
        } catch (err) {
            document.getElementById(loadingId).remove();
            addChatMessage('Sorry, I encountered an error. Please try again.', 'ai');
        }
    });
}

function addChatMessage(text, sender) {
    const history = document.getElementById('chat-history');
    const div = document.createElement('div');
    div.className = `message ${sender}-message`;
    
    const content = document.createElement('div');
    content.className = 'message-content';
    content.textContent = text; // Safe against XSS
    
    div.appendChild(content);
    history.appendChild(div);
    history.scrollTop = history.scrollHeight;
}

// Data Loading
async function loadDashboard() {
    if (!userId) return;
    try {
        const progress = await apiCall(`/users/${userId}/progress`);
        document.getElementById('dash-total-solved').textContent = progress.problemsSolved || 0;
        document.getElementById('dash-attempted').textContent = progress.problemsAttempted || 0;
        document.getElementById('dash-streak').innerHTML = `${progress.currentStreak || 0} <span class="streak-icon">🔥</span>`;
        document.getElementById('dash-hints').textContent = progress.hintsUsed || 0;
        
        document.getElementById('dash-easy').textContent = progress.easySolved || 0;
        document.getElementById('dash-medium').textContent = progress.mediumSolved || 0;
        document.getElementById('dash-hard').textContent = progress.hardSolved || 0;

        // Load Recommendations
        try {
            const recs = await apiCall(`/users/${userId}/recommendations`);
            const recContainer = document.getElementById('dash-recommendation');
            if (recs && recs.recommendedTopic) {
                recContainer.innerHTML = `
                    <div class="rec-topic">${recs.recommendedTopic}</div>
                    ${recs.recommendedProblem ? `
                        <div class="problem-card">
                            <div class="problem-header">
                                <span class="problem-title">${recs.recommendedProblem.title}</span>
                                <span class="badge ${recs.recommendedProblem.difficulty.toLowerCase()}">${recs.recommendedProblem.difficulty}</span>
                            </div>
                            <p class="text-muted" style="font-size: 0.875rem; margin-bottom: 1rem;">${recs.recommendedProblem.description.substring(0, 100)}...</p>
                            <button class="btn btn-primary" style="padding: 0.5rem 1rem; font-size: 0.875rem;" onclick="switchView('problems-view')">Solve Now</button>
                        </div>
                    ` : '<p>Keep up the good work! No specific problem recommended right now.</p>'}
                `;
            } else {
                recContainer.innerHTML = '<p>Start solving problems to get personalized recommendations!</p>';
            }
        } catch (e) {
            document.getElementById('dash-recommendation').innerHTML = '<p class="text-muted">Unable to load recommendations.</p>';
        }

    } catch (err) {
        console.error('Failed to load dashboard', err);
    }
}

async function loadProblems() {
    try {
        const diffFilter = document.getElementById('problem-difficulty-filter').value;
        const topicFilter = document.getElementById('problem-topic-filter').value;
        
        let url = '/problems';
        if (topicFilter) {
            url = `/problems/topic/${topicFilter}`;
            // Client side filtering for difficulty since backend might not support combo filters
        } else if (diffFilter) {
            url = `/problems/difficulty/${diffFilter}`;
        }

        const problems = await apiCall(url);
        const container = document.getElementById('problems-list');
        
        if (problems.length === 0) {
            container.innerHTML = '<p class="text-muted">No problems found.</p>';
            return;
        }

        // Apply secondary client side filter if needed
        let filteredProblems = problems;
        if (topicFilter && diffFilter) {
            filteredProblems = problems.filter(p => p.difficulty === diffFilter);
        }

        container.innerHTML = filteredProblems.map(p => `
            <div class="problem-card">
                <div class="problem-header">
                    <div class="problem-title">${p.title}</div>
                    <div class="badge ${p.difficulty.toLowerCase()}">${p.difficulty}</div>
                </div>
                <div class="problem-topic">${p.topic}</div>
                <p style="font-size: 0.875rem; color: var(--text-muted); margin-bottom: 1rem;">
                    ${p.description.substring(0, 80)}...
                </p>
                <button class="btn btn-primary" onclick="alert('Coding environment coming soon!')">Solve Problem</button>
            </div>
        `).join('');

    } catch (err) {
        document.getElementById('problems-list').innerHTML = '<p class="text-muted">Failed to load problems.</p>';
    }
}

async function loadProgress() {
    if (!userId) return;
    try {
        const progress = await apiCall(`/users/${userId}/progress`);
        document.getElementById('prog-solved').textContent = progress.problemsSolved || 0;
        document.getElementById('prog-topics').textContent = progress.topicsStudied || 0;
        document.getElementById('prog-streak').textContent = progress.currentStreak || 0;
        
        const date = progress.lastActivity ? new Date(progress.lastActivity).toLocaleDateString() : 'Never';
        document.getElementById('prog-last-active').textContent = date;
    } catch (err) {
        console.error('Failed to load progress', err);
    }
}

// Add event listeners for filters
document.getElementById('problem-difficulty-filter').addEventListener('change', loadProblems);
document.getElementById('problem-topic-filter').addEventListener('change', loadProblems);

// Start
document.addEventListener('DOMContentLoaded', init);
