// src/context/AuthContext.js
import React, { createContext, useState } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [role, setRole] = useState(localStorage.getItem("role") || null);
  const [doctorId,setDoctorId] =useState(localStorage.getItem("doctorId")||null);
    const [patientId,setPatientId] =useState(localStorage.getItem("patientId")||null);

  const login = (token, role,doctorId,patientId) => {
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
      localStorage.setItem("doctorId", doctorId);
      localStorage.setItem("patientId", patientId);


    setToken(token);
    setRole(role);
    setDoctorId(doctorId);
    setPatientId(patientId);
  };
const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  localStorage.removeItem("doctorId");
  localStorage.removeItem("patientId");

  setToken(null);
  setRole(null);
  setDoctorId(null);
  setPatientId(null);
  
};

 
  return (
    <AuthContext.Provider value={{ token, role,doctorId, login,patientId,logout }}>
      {children}
    </AuthContext.Provider>
  );
}
