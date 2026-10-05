import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/api";

const BillingDetails = () => {
  const { appointmentId } = useParams();
  const [billing, setBilling] = useState(null);
  const [prescription, setPrescription] = useState(null);
  const [error, setError] = useState("");

  const [amount, setAmount] = useState("");
  const [status, setStatus] = useState("PENDING");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const prescriptionRes = await api.get(
          `/api/prescription/appointment/${appointmentId}`,
        );
        if (!prescriptionRes.data.data) {
          setError(
            "❌ Prescription not found for this appointment. Billing cannot be generated.",
          );
          return;
        }
        setPrescription(prescriptionRes.data.data);

        // Step 2: Try fetching billing
        try {
          const billingRes = await api.get(
            `/api/billing/appointment/${appointmentId}`,
          );
          if (billingRes.data.data) {
            setBilling(billingRes.data.data);
            setAmount(billingRes.data.data.amount);
            setStatus(billingRes.data.data.status);
          }
        } catch (billingError) {
          console.log("No billing found, admin can create new one.");
        }
      } catch (error) {
        setError(
          error.response?.data?.message || "Error fetching prescription",
        );
      }
    };
    fetchData();
  }, [appointmentId]);

  // Create new billing
  const generateBilling = async () => {
    try {
      const response = await api.post(
        `/api/billing/appointment/${appointmentId}`,
        { amount: parseFloat(amount), status },
      );
      setBilling(response.data.data);
      alert(response?.data.message);
    } catch (error) {
      setError(error.response?.data?.message || "Error generating billing");
    }
  };

  // Update existing billing
  const updateBilling = async () => {
    try {
      const response = await api.put(`/api/billing/${billing.id}`, {
        ...billing,
        amount: parseFloat(amount),
        status,
      });
      setBilling(response.data.data);
      alert(response.data.message);
    } catch (error) {
      setError(error.response?.data?.message || "Error updating billing");
    }
  };

  if (error) return <p style={styles.errorMsg}>{error}</p>;

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Billing & Prescription Details</h2>

      {/* Prescription Section */}
      {prescription && (
        <div style={styles.card}>
          <h3 style={styles.subHeading}>Prescription</h3>
          <p>
            <strong>Medicines:</strong> {prescription.medicines}
          </p>
          <p>
            <strong>Dosage:</strong> {prescription.dosage}
          </p>
          <p>
            <strong>Notes:</strong> {prescription.notes}
          </p>
          <p>
            <p>
              <strong>Date:</strong>{" "}
              {new Date(prescription.date).toLocaleString("en-IN")}
            </p>{" "}
          </p>
        </div>
      )}

      {/* Billing Section */}
      <div style={styles.card}>
        <h3 style={styles.subHeading}>
          {billing ? "Update Billing" : "Generate Billing"}
        </h3>
        <div style={styles.formGroup}>
          <label>Amount (₹):</label>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            style={styles.input}
          />
        </div>
        <div style={styles.formGroup}>
          <label>Status:</label>
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            style={styles.select}
          >
            <option value="PENDING">Pending</option>
            <option value="PAID">Paid</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>
        {!billing ? (
          <button style={styles.button} onClick={generateBilling}>
            Generate Billing
          </button>
        ) : (
          <button style={styles.buttonSecondary} onClick={updateBilling}>
            Update Billing
          </button>
        )}
      </div>
    </div>
  );
};

export default BillingDetails;

const styles = {
  container: {
    padding: "40px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    background: "linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)",
    minHeight: "100vh",
  },
  heading: { textAlign: "center", color: "#2c3e50", marginBottom: "30px" },
  subHeading: { color: "#34495e", marginBottom: "10px" },
  card: {
    background: "#fff",
    padding: "20px",
    borderRadius: "8px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    marginBottom: "20px",
  },
  formGroup: { marginBottom: "15px" },
  input: {
    padding: "8px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    width: "100%",
  },
  select: {
    padding: "8px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    width: "100%",
  },
  button: {
    padding: "10px 15px",
    background: "linear-gradient(135deg, #43cea2 0%, #185a9d 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
    marginRight: "10px",
  },
  buttonSecondary: {
    padding: "10px 15px",
    background: "linear-gradient(135deg, #f7971e 0%, #ffd200 100%)",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "600",
  },
  loading: {
    textAlign: "center",
    color: "#3498db",
    marginTop: "20px",
    fontSize: "18px",
  },
  errorMsg: { textAlign: "center", color: "#e74c3c", marginTop: "10px" },
};
