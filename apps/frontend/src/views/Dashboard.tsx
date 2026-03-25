import { useTranslation } from "react-i18next";
import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  Stack,
  Typography,
} from "@mui/material";
import TrendingUpRoundedIcon from "@mui/icons-material/TrendingUpRounded";
import GroupsRoundedIcon from "@mui/icons-material/GroupsRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";

export function Dashboard() {
  const { t } = useTranslation();

  return (
    <Stack spacing={3}>
      <Card>
        <CardContent sx={{ p: { xs: 3, sm: 4 } }}>
          <Stack spacing={2}>
            <Typography
              variant="h3"
              component="h1"
              sx={{
                fontWeight: 800,
                lineHeight: 1.15,
                background:
                  "linear-gradient(105deg, #00F0FF 0%, #7C4DFF 42%, #FF453A 100%)",
                WebkitBackgroundClip: "text",
                WebkitTextFillColor: "transparent",
                backgroundClip: "text",
              }}
            >
              {t("dashboard.heroTitle")}
            </Typography>
            <Typography
              variant="body1"
              color="text.secondary"
              sx={{ maxWidth: (theme) => theme.breakpoints.values.md }}
            >
              {t("dashboard.heroDescription")}
            </Typography>
            <Box>
              <Button variant="contained" color="primary" size="large">
                {t("dashboard.newReservation")}
              </Button>
            </Box>
          </Stack>
        </CardContent>
      </Card>

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <TrendingUpRoundedIcon color="primary" />
                <Typography
                  variant="subtitle2"
                  color="text.secondary"
                  fontWeight={600}
                >
                  {t("dashboard.stats.revenueTitle")}
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                128 400 zł
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {t("dashboard.stats.revenueHint")}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <GroupsRoundedIcon color="primary" />
                <Typography
                  variant="subtitle2"
                  color="text.secondary"
                  fontWeight={600}
                >
                  {t("dashboard.stats.clientsTitle")}
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                342
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {t("dashboard.stats.clientsHint")}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <CalendarMonthRoundedIcon color="primary" />
                <Typography
                  variant="subtitle2"
                  color="text.secondary"
                  fontWeight={600}
                >
                  {t("dashboard.stats.reservationsTodayTitle")}
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                14
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {t("dashboard.stats.reservationsTodayHint")}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent>
              <Typography variant="h6" fontWeight={700} gutterBottom>
                {t("dashboard.fleetCardTitle")}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {t("dashboard.fleetCardBody")}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent>
              <Typography variant="h6" fontWeight={700} gutterBottom>
                {t("dashboard.reservationsCardTitle")}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {t("dashboard.reservationsCardBody")}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Stack>
  );
}
