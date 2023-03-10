import { Container } from 'react-bootstrap';
import LoginModal from "./components/LoginModal";
import NavBar from './components/NavBar';
import SignUpModal from './components/SignUpModal';
import styles from "./styles/App.module.css";
import { useEffect, useState } from "react";
import { Admin } from './models/admin';
import * as EventsApi from "./network/events_api";
import EventsPageLoggedInView from './components/EventsPageLoggedInView';
import EventsPageLoggedOutView from './components/EventsPageLoggedOutView';
import { BrowserRouter } from 'react-router-dom';
import { Route, Routes } from 'react-router';
import EventsPage from './pages/EventsPage';
import PrivacyPage from './pages/PrivacyPage';
import NotFoundPage from './pages/NotFoundPage';

function App() {

  const [loggedInAdmin, setLoggedInAdmin] = useState<Admin|null>(null);

  const [showSignUpModal, setShowSignUpModal] = useState(false);
  
  const [showLoginModal, setShowLoginModal] = useState(false);

  useEffect(() => {
    async function fetchLoggedInAdmin(){
      try {
        const admin = await EventsApi.getLoggedInAdmin();
        setLoggedInAdmin(admin);
      }catch(error){
        console.error(error);

      }
    }
    fetchLoggedInAdmin();
  },[]);

  return (
    <BrowserRouter>
    <div>
      <NavBar
        loggedInAdmin={loggedInAdmin}
        onSignUpClicked={()=>setShowSignUpModal(true)}
        onLoginClicked={()=>setShowLoginModal(true)}
        onLogoutSuccessful={()=>setLoggedInAdmin(null)}
      />
      <Container className={styles.pageContainer}>
        <Routes>
          <Route 
            path='/'
            element={<EventsPage loggedInAdmin={loggedInAdmin}/>}
          />
          <Route 
            path='/privacy'
            element={<PrivacyPage/>}
          />
          <Route 
            path='/*'
            element={<NotFoundPage />}
          />
        </Routes>
      </Container>
    {
      showSignUpModal &&
      <SignUpModal 
        onDismiss={()=>setShowSignUpModal(false)}
        onSignUpSuccessful={(admin)=>{
          setLoggedInAdmin(admin);
          setShowSignUpModal(false);
        }}
      />
    }
    {
      showLoginModal &&
      <LoginModal 
        onDismiss={()=>setShowLoginModal(false)}
        onLoginSuccessful={(admin)=>{
          setLoggedInAdmin(admin);
          setShowLoginModal(false);
        }}
      />
    }
    </div>
    </BrowserRouter>
  );
}

export default App;
