import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa6";
import { Container, Box, Typography, TextField, Button, IconButton, Paper, InputAdornment } from "@mui/material";
import Image from "../assets/image.png";
import Logo from "../assets/logo.png";

export const PATH = "http://10.14.171.25:8004/duy/testcatalog1/testoauh2";
export const CLIENT_ID = "E9GIlesUNP9L8Ttmc5sab2atC5gUavIe";
export const BASE_URL = "http://localhost:8989";

const Login = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [path, setPath] = useState("");
  const [clientId , setClientId] = useState("");
  useEffect(() => {
    const queryParams = new URLSearchParams(window.location.search);
    const pathFromUrl = queryParams.get("path") != null ? queryParams.get("path") : PATH;
    const clientIdFromUrl = queryParams.get("clientIdFromUrl") != null ? queryParams.get("clientIdFromUrl") : CLIENT_ID;
    if (pathFromUrl) {
      setPath(pathFromUrl);
      setClientId(clientIdFromUrl);
    }
  }, []);
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await fetch(`${BASE_URL}/auth/login?path=${path}&clientId=${clientId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      });

      const data = await response.json();
      if (!response.ok) throw new Error(data.message || "Login failed");

      localStorage.setItem("accessToken", data.accessToken);
      localStorage.setItem("clientId", data.clientId);
      localStorage.setItem("clientSecret", data.clientSecret);
      localStorage.setItem("authorizationUserid", data.authorizationUserid);
      localStorage.setItem("provisionKey", data.provisionKey);
      localStorage.setItem("path", data.path);
      localStorage.setItem("oauthTokenLogDtos", JSON.stringify(data.oauthTokenLogDtos));
      alert("Login successful!");
      navigate("/authorize");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ display: "flex", overflow: "hidden", mt: 8, borderRadius: 2 }}>
        <Box
          sx={{
            display: { xs: "none", md: "block" },
            width: "50%",
            backgroundImage: `url(${Image})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        />
        <Box sx={{ p: 4, width: { xs: "100%", md: "50%" }, display: "flex", flexDirection: "column", alignItems: "center" }}>
          <img src={Logo} alt="Logo" style={{ width: "80px", marginBottom: "16px" }} />
          <Typography variant="h5" gutterBottom fontWeight="bold">
            Welcome Back!
          </Typography>
          <Typography variant="body2" color="textSecondary" gutterBottom>
            Sign in with your credentials or OAuth2
          </Typography>
          {error && <Typography color="error">{error}</Typography>}
          <Box component="form" onSubmit={handleLogin} sx={{ width: "100%", mt: 2 }}>
            <TextField
              fullWidth
              label="Username"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              margin="normal"
              required
              variant="outlined"
              sx={{ backgroundColor: "white", borderRadius: "8px" }}
            />
            <TextField
              fullWidth
              label="Password"
              type={showPassword ? "text" : "password"}
              name="password"
              value={credentials.password}
              onChange={handleChange}
              margin="normal"
              required
              variant="outlined"
              sx={{ backgroundColor: "white", borderRadius: "8px" }}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={() => setShowPassword(!showPassword)}>
                      {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Button
              fullWidth
              type="submit"
              variant="contained"
              color="primary"
              disabled={loading}
              sx={{ mt: 2, borderRadius: "8px", fontWeight: "bold", fontSize: "16px" }}
            >
              {loading ? "Logging in..." : "Log In"}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login;