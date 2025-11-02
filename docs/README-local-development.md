# Local Documentation Development

## Quick Setup

The documentation site is automatically built and deployed via GitHub Actions, but you can also build it locally for testing.

### Install Jekyll (Optional)

```bash
# Install Ruby and Jekyll
gem install jekyll bundler

# Navigate to docs directory
cd docs

# Install dependencies
bundle install

# Build the site locally
bundle exec jekyll build

# Serve locally (with auto-rebuild)
bundle exec jekyll serve
```

### Automatic Deployment

The site automatically rebuilds when you push to `main` branch:

1. **Add/edit** markdown files in `docs/`
2. **Commit and push** to main branch
3. **GitHub Actions** automatically builds and deploys
4. **Site updates** at https://gcclinux.github.io/smalltextpad/

### File Structure

```
docs/
├── _config.yml          # Jekyll configuration
├── index.md            # Homepage
├── README-*.md         # Platform-specific guides
└── _site/              # Generated site (auto-built)
```

### Adding New Documentation

1. Create new `.md` files in `docs/`
2. Use Jekyll front matter if needed:
   ```yaml
   ---
   title: Page Title
   description: Page description
   ---
   ```
3. Commit and push - automatic deployment handles the rest!