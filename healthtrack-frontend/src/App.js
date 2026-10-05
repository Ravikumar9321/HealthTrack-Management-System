import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import PrivateRoute from "./routes/PrivateRoute";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Appointment from "./pages/doctor/Appointments";
import PatientDetails from "./pages/doctor/PatientDetails";
import DoctorProfile from "./pages/doctor/DoctorProfile";
import AdminDashBoard from "./pages/admin/AdminDashBoard";
import BillingDetails from "./pages/billing/BillingDetails";
import PatientDashboard from "./pages/Patient/PatientDashboard";
import BookAppointments from "./pages/Appointment/BookAppointment";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected routes grouped */}
          <Route element={<PrivateRoute />}>
            <Route path="/appointments" element={<Appointment />} />
            <Route
              path="/patient/:appointmentId"
              element={<PatientDetails />}
            />
            <Route path="/profile" element={<DoctorProfile />} />
            <Route path="/admin" element={<AdminDashBoard />} />
            <Route
              path="/billing/:appointmentId"
              element={<BillingDetails />}
            />
            <Route path="/patientDashboard" element={<PatientDashboard />} />
            <Route path="/bookAppointment" element={<BookAppointments />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
