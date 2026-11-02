package com.fot.tms.ui.common;

import com.fot.tms.model.Profile;
import com.fot.tms.model.User;
import com.fot.tms.service.AuthService;
import com.fot.tms.service.UserService;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;
import com.fot.tms.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import java.io.File;



public class ProfilePanel extends JPanel {
 private final UserService userService;
 private final User currentUser;

 private JTextField txtUsername;
 private JTextField txtRole;
 private JTextField txtEmail;
 private JTextField txtFirstName;
 private JTextField txtLastName;
 private JTextField txtContactNo;
 private JTextField txtPhotoPath;
 private JLabel lblAvatar;
 private JButton btnSave;
 private JButton btnBrowsePhoto;
 private JButton btnRemovePhoto;
 private JButton btnChangePassword;

 public ProfilePanel() {
 this.userService = new UserService();
 this.currentUser = AuthService.getInstance().getCurrentUser();
 initComponents();
 loadUserData();
 }

 private void initComponents() {
 setLayout(new BorderLayout(20, 20));
 setBackground(UIConstants.BG_MAIN);
 setBorder(new EmptyBorder(25, 25, 25, 25));


 JPanel headerPanel = new JPanel(new BorderLayout());
 headerPanel.setOpaque(false);
 JLabel lblTitle = new JLabel("My Profile & Account Details");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

 JLabel lblSub = new JLabel("Manage your contact information and profile settings.");
 lblSub.setFont(UIConstants.FONT_REGULAR);
 lblSub.setForeground(UIConstants.TEXT_MUTED);

 headerPanel.add(lblTitle, BorderLayout.NORTH);
 headerPanel.add(lblSub, BorderLayout.SOUTH);
 add(headerPanel, BorderLayout.NORTH);


 JPanel card = UIHelper.createCardPanel();
 card.setLayout(new GridBagLayout());
 GridBagConstraints gbc = new GridBagConstraints();
 gbc.insets = new Insets(8, 12, 8, 12);
 gbc.fill = GridBagConstraints.HORIZONTAL;


 JPanel avatarPanel = new JPanel();
 avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
 avatarPanel.setOpaque(false);

 lblAvatar = new JLabel("", SwingConstants.CENTER);
 lblAvatar.setPreferredSize(new Dimension(110, 110));
 lblAvatar.setMaximumSize(new Dimension(110, 110));
 lblAvatar.setMinimumSize(new Dimension(110, 110));
 lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
 lblAvatar.setOpaque(true);
 lblAvatar.setBackground(new Color(224, 231, 255));
 lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 36));
 lblAvatar.setForeground(UIConstants.PRIMARY_LIGHT);
 lblAvatar.setBorder(new LineBorder(UIConstants.PRIMARY_LIGHT, 3, true));
 avatarPanel.add(lblAvatar);

 avatarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

 JPanel photoActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
 photoActionPanel.setOpaque(false);
 btnBrowsePhoto = UIHelper.createPrimaryButton(" Upload New Photo");
 btnBrowsePhoto.addActionListener(e -> choosePhoto());
 photoActionPanel.add(btnBrowsePhoto);

 btnRemovePhoto = UIHelper.createSecondaryButton("Remove Photo");
 btnRemovePhoto.setVisible(false);
 btnRemovePhoto.addActionListener(e -> removePhoto());
 photoActionPanel.add(btnRemovePhoto);

 avatarPanel.add(photoActionPanel);

 gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
 card.add(avatarPanel, gbc);

 gbc.gridwidth = 1;
 gbc.anchor = GridBagConstraints.WEST;


 gbc.gridx = 0; gbc.gridy = 1;
 card.add(new JLabel("Username (Registration No):"), gbc);
 gbc.gridx = 1;
 txtUsername = UIHelper.createStyledTextField(25);
 txtUsername.setEditable(false);
 txtUsername.setBackground(new Color(241, 245, 249));
 card.add(txtUsername, gbc);


 gbc.gridx = 0; gbc.gridy = 2;
 card.add(new JLabel("Role:"), gbc);
 gbc.gridx = 1;
 txtRole = UIHelper.createStyledTextField(25);
 txtRole.setEditable(false);
 txtRole.setBackground(new Color(241, 245, 249));
 card.add(txtRole, gbc);


 gbc.gridx = 0; gbc.gridy = 3;
 card.add(new JLabel("First Name:"), gbc);
 gbc.gridx = 1;
 txtFirstName = UIHelper.createStyledTextField(25);
 card.add(txtFirstName, gbc);


 gbc.gridx = 0; gbc.gridy = 4;
 card.add(new JLabel("Last Name:"), gbc);
 gbc.gridx = 1;
 txtLastName = UIHelper.createStyledTextField(25);
 card.add(txtLastName, gbc);


 gbc.gridx = 0; gbc.gridy = 5;
 card.add(new JLabel("Email Address:"), gbc);
 gbc.gridx = 1;
 txtEmail = UIHelper.createStyledTextField(25);
 card.add(txtEmail, gbc);


 gbc.gridx = 0; gbc.gridy = 6;
 card.add(new JLabel("Contact Number:"), gbc);
 gbc.gridx = 1;
 txtContactNo = UIHelper.createStyledTextField(25);
 card.add(txtContactNo, gbc);


 txtPhotoPath = new JTextField();
 txtPhotoPath.setVisible(false);


 gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
 JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
 btnPanel.setOpaque(false);

 btnChangePassword = UIHelper.createSecondaryButton("Change Password");
 btnChangePassword.addActionListener(e -> openChangePasswordDialog());

 btnSave = UIHelper.createPrimaryButton("Save Profile Changes");
 btnSave.addActionListener(e -> saveProfile());

 btnPanel.add(btnChangePassword);
 btnPanel.add(btnSave);
 card.add(btnPanel, gbc);


 JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
 centerWrapper.setOpaque(false);
 centerWrapper.add(card);

 JScrollPane scrollPane = new JScrollPane(centerWrapper);
 scrollPane.setBorder(null);
 scrollPane.setOpaque(false);
 scrollPane.getViewport().setOpaque(false);
 add(scrollPane, BorderLayout.CENTER);

 applyRoleRestrictions();
 }

 private void applyRoleRestrictions() {
 if (currentUser == null) return;
 String role = currentUser.getRole();


 if (!currentUser.canChangeOwnPassword()) {
 btnChangePassword.setVisible(false);
 btnChangePassword.setEnabled(false);
 }


 if (!currentUser.canEditProfileNames()) {
 txtFirstName.setEditable(false);
 txtFirstName.setBackground(new Color(241, 245, 249));
 txtLastName.setEditable(false);
 txtLastName.setBackground(new Color(241, 245, 249));
 txtEmail.setEditable(false);
 txtEmail.setBackground(new Color(241, 245, 249));
 } 

 if ("Lecturer".equalsIgnoreCase(role) || "TO".equalsIgnoreCase(role)) {
 txtUsername.setEditable(false);
 txtRole.setEditable(false);
 }
 }

 private void loadUserData() {
 if (currentUser == null) return;
 User user = userService.getUserById(currentUser.getUserId());
 if (user == null) user = currentUser;

 txtUsername.setText(user.getUsername());
 txtRole.setText(user.getRole());
 txtEmail.setText(user.getEmail());

 Profile p = user.getProfile();
 if (p != null) {
 txtFirstName.setText(p.getFirstName());
 txtLastName.setText(p.getLastName());
 txtContactNo.setText(p.getContactNo());
 txtPhotoPath.setText(p.getProfilePicturePath());
 displayProfileImage(p.getProfilePicturePath(), p.getFirstName(), p.getLastName(), user.getUsername());
 } else {
 displayProfileImage(null, "", "", user.getUsername());
 }
 }

 private void displayProfileImage(String photoPath, String fn, String ln, String un) {
 File file = null;
 if (photoPath != null && !photoPath.trim().isEmpty()) {
 file = new File(photoPath);
 if (!file.exists()) {
 file = new File(System.getProperty("user.dir"), photoPath);
 }
 if (!file.exists()) {
 file = new File(new File(System.getProperty("user.dir"), "profile_pictures"), new File(photoPath).getName());
 }
 }

 if (file != null && file.exists() && file.isFile()) {
 try {
 BufferedImage original = ImageIO.read(file);
 if (original != null) {
 ImageIcon circularIcon = createCircularAvatar(original, 110);
 lblAvatar.setIcon(circularIcon);
 lblAvatar.setText("");
 lblAvatar.setBackground(Color.WHITE);
 if (btnRemovePhoto != null) btnRemovePhoto.setVisible(true);
 return;
 }
 } catch (Exception ex) {
 System.err.println("Error loading profile image: " + ex.getMessage());
 }
 }


 lblAvatar.setIcon(null);
 lblAvatar.setBackground(new Color(224, 231, 255));
 String initials = "";
 if (fn != null && !fn.isEmpty()) initials += fn.substring(0, 1).toUpperCase();
 if (ln != null && !ln.isEmpty()) initials += ln.substring(0, 1).toUpperCase();
 if (initials.isEmpty() && un != null && !un.isEmpty()) initials = un.substring(0, 1).toUpperCase();
 lblAvatar.setText(initials.isEmpty() ? "U" : initials);
 if (btnRemovePhoto != null) btnRemovePhoto.setVisible(false);
 }

 private ImageIcon createCircularAvatar(BufferedImage src, int size) {
 BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
 Graphics2D g2 = output.createGraphics();
 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);


 g2.setClip(new Ellipse2D.Float(0, 0, size, size));
 

 double scale = Math.max((double) size / src.getWidth(), (double) size / src.getHeight());
 int drawW = (int) (src.getWidth() * scale);
 int drawH = (int) (src.getHeight() * scale);
 int x = (size - drawW) / 2;
 int y = (size - drawH) / 2;
 g2.drawImage(src, x, y, drawW, drawH, null);


 g2.setClip(null);
 g2.setColor(UIConstants.PRIMARY_LIGHT);
 g2.setStroke(new BasicStroke(3));
 g2.draw(new Ellipse2D.Float(1.5f, 1.5f, size - 3f, size - 3f));
 g2.dispose();

 return new ImageIcon(output);
 }

 private void choosePhoto() {
 JFileChooser chooser = new JFileChooser();
 chooser.setDialogTitle("Select Profile Picture (PNG, JPG, JPEG)");
 chooser.setFileFilter(new FileNameExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));
 int res = chooser.showOpenDialog(this);
 if (res == JFileChooser.APPROVE_OPTION) {
 File selectedFile = chooser.getSelectedFile();
 try {

 File dir = new File(System.getProperty("user.dir"), "profile_pictures");
 if (!dir.exists()) dir.mkdirs();


 String ext = "jpg";
 String name = selectedFile.getName();
 int dotIdx = name.lastIndexOf('.');
 if (dotIdx > 0) ext = name.substring(dotIdx + 1);

 String destFileName = "user_" + (currentUser != null ? currentUser.getUserId() : 0) + "_" + System.currentTimeMillis() + "." + ext;
 File destFile = new File(dir, destFileName);

 Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

 String relPath = "profile_pictures/" + destFileName;
 txtPhotoPath.setText(relPath);


 displayProfileImage(destFile.getAbsolutePath(), txtFirstName.getText(), txtLastName.getText(), currentUser != null ? currentUser.getUsername() : "");
 UIHelper.showInfo(this, "Profile picture selected! Click 'Save Changes' to update your account.", "Photo Ready");
 } catch (Exception ex) {
 UIHelper.showError(this, "Failed to upload photo: " + ex.getMessage(), "Upload Error");
 }
 }
 }

 private void removePhoto() {
 txtPhotoPath.setText("");
 displayProfileImage(null, txtFirstName.getText(), txtLastName.getText(), currentUser != null ? currentUser.getUsername() : "");
 UIHelper.showInfo(this, "Profile picture removed. Click 'Save Changes' to apply.", "Photo Removed");
 }

 private void openChangePasswordDialog() {
 ChangePasswordDialog dialog = new ChangePasswordDialog((Frame) SwingUtilities.getWindowAncestor(this), currentUser);
 dialog.setVisible(true);
 }

 private void saveProfile() {
 if (currentUser == null) return;
 String email = txtEmail.getText().trim();
 String contact = txtContactNo.getText().trim();
 String photo = txtPhotoPath.getText().trim();
 String fn = txtFirstName.getText().trim();
 String ln = txtLastName.getText().trim();

 if (!ValidationUtil.isValidPhone(contact)) {
 UIHelper.showWarning(this, "Please enter a valid phone number (e.g. +94771234567 or 0771234567).", "Invalid Contact");
 return;
 }

 String role = currentUser.getRole();
 boolean success;

 if ("Undergraduate".equalsIgnoreCase(role)) {
 success = userService.updateContactAndPhoto(currentUser.getUserId(), contact, photo);
 } else {
 if (!ValidationUtil.isValidEmail(email)) {
 UIHelper.showWarning(this, "Please enter a valid email address.", "Invalid Email");
 return;
 }
 User u = new User();
 u.setUserId(currentUser.getUserId());
 u.setUsername(currentUser.getUsername());
 u.setEmail(email);
 u.setRole(currentUser.getRole());
 u.setStatus(currentUser.getStatus());

 Profile p = new Profile();
 p.setUserId(currentUser.getUserId());
 p.setFirstName(fn);
 p.setLastName(ln);
 p.setContactNo(contact);
 p.setProfilePicturePath(photo);

 success = userService.updateUser(u, p);
 }

 if (success) {
 UIHelper.showInfo(this, "Profile updated successfully!", "Success");
 displayProfileImage(photo, fn, ln, currentUser.getUsername());

 User refreshed = userService.getUserById(currentUser.getUserId());
 AuthService.getInstance().setCurrentUser(refreshed);
 } else {
 UIHelper.showError(this, "Failed to update profile. Please try again.", "Error");
 }
 }
}
